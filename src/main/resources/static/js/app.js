// ==========================================================================
// VaxGuard Frontend Application Logic
// DevOps Capstone Project | Gayatri Patil (23102A0016)
// ==========================================================================

let currentRole = 'HEALTH_WORKER'; // 'HEALTH_WORKER' or 'PARENT'
let vaccineChart = null;

document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    initRoleSwitcher();
    initModals();
    initSearchAndFilter();
    initDefectModeToggle();
    initReminders();

    // Initial Data Fetch
    loadDashboardStats();
    loadVaccinationRecords();
    loadBeneficiaries();
});

// --- 1. NAVIGATION TABS ---
function initNavigation() {
    const tabs = [
        { btn: 'btn-nav-dashboard', content: 'tab-dashboard' },
        { btn: 'btn-nav-schedule', content: 'tab-schedule' },
        { btn: 'btn-nav-beneficiaries', content: 'tab-beneficiaries' },
        { btn: 'btn-nav-reminders', content: 'tab-reminders' },
        { btn: 'btn-nav-devops', content: 'tab-devops' }
    ];

    tabs.forEach(tab => {
        const btn = document.getElementById(tab.btn);
        if (btn) {
            btn.addEventListener('click', () => {
                tabs.forEach(t => {
                    const b = document.getElementById(t.btn);
                    const c = document.getElementById(t.content);
                    if (b) b.classList.remove('active');
                    if (c) c.style.display = 'none';
                });

                btn.classList.add('active');
                const contentEl = document.getElementById(tab.content);
                if (contentEl) contentEl.style.display = 'block';

                if (tab.content === 'tab-schedule') {
                    loadVaccinationRecords();
                } else if (tab.content === 'tab-beneficiaries') {
                    loadBeneficiaries();
                } else if (tab.content === 'tab-dashboard') {
                    loadDashboardStats();
                }
            });
        }
    });
}

// --- 2. ROLE SWITCHER ---
function initRoleSwitcher() {
    const hwBtn = document.getElementById('role-healthworker-btn');
    const parentBtn = document.getElementById('role-parent-btn');

    if (hwBtn && parentBtn) {
        hwBtn.addEventListener('click', () => {
            currentRole = 'HEALTH_WORKER';
            hwBtn.classList.add('active');
            parentBtn.classList.remove('active');
            showToast('Switched to Healthcare Worker (Clinical Admin) Mode', 'info');
            loadVaccinationRecords();
        });

        parentBtn.addEventListener('click', () => {
            currentRole = 'PARENT';
            parentBtn.classList.add('active');
            hwBtn.classList.remove('active');
            showToast('Switched to Parent / Beneficiary View (Read Only)', 'info');
            loadVaccinationRecords();
        });
    }
}

// --- 3. DASHBOARD METRICS & CHART ---
async function loadDashboardStats() {
    try {
        const res = await fetch('/api/dashboard/stats');
        if (!res.ok) throw new Error('Failed to fetch dashboard statistics');
        const data = await res.json();

        // Update KPI values
        document.getElementById('stat-total-beneficiaries').textContent = data.totalBeneficiaries || 0;
        document.getElementById('stat-completed-vaccines').textContent = data.completedCount || 0;
        document.getElementById('stat-upcoming-vaccines').textContent = data.upcomingCount || 0;
        document.getElementById('stat-overdue-vaccines').textContent = data.overdueCount || 0;
        document.getElementById('stat-compliance-rate').textContent = data.complianceRate || 0;

        renderChart(data);
    } catch (err) {
        console.error('Error loading stats:', err);
    }
}

function renderChart(data) {
    const canvas = document.getElementById('vaccineChart');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    if (vaccineChart) {
        vaccineChart.destroy();
    }

    const completed = data.completedCount || 0;
    const scheduled = data.scheduledCount || 0;
    const overdue = data.overdueCount || 0;

    vaccineChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Administered (Completed)', 'Upcoming / Scheduled', 'Overdue Doses'],
            datasets: [{
                data: [completed, scheduled, overdue],
                backgroundColor: [
                    '#10b981', // Emerald
                    '#0ea5e9', // Sky blue
                    '#f43f5e'  // Rose red
                ],
                borderColor: '#0f172a',
                borderWidth: 3,
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        color: '#94a3b8',
                        font: { family: "'Plus Jakarta Sans', sans-serif", size: 12 },
                        padding: 16
                    }
                }
            },
            cutout: '70%'
        }
    });
}

// --- 4. VACCINATION RECORDS TABLE ---
async function loadVaccinationRecords() {
    const searchVal = document.getElementById('search-input')?.value || '';
    const statusVal = document.getElementById('status-filter')?.value || 'ALL';

    const url = `/api/vaccinations?search=${encodeURIComponent(searchVal)}&status=${encodeURIComponent(statusVal)}`;

    try {
        const res = await fetch(url);
        if (!res.ok) throw new Error('Failed to load records');
        const records = await res.json();
        renderRecordsTable(records);
    } catch (err) {
        console.error('Error loading records:', err);
    }
}

function renderRecordsTable(records) {
    const tbody = document.getElementById('records-table-body');
    if (!tbody) return;

    if (records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" style="text-align:center; padding:32px; color:var(--text-dim);">No vaccination records match your query.</td></tr>`;
        return;
    }

    tbody.innerHTML = records.map(r => {
        const statusBadgeClass = r.status === 'COMPLETED' ? 'badge-completed' :
                                 r.status === 'OVERDUE' ? 'badge-overdue' : 'badge-scheduled';

        const adminDetails = r.administeredDate 
            ? `<div style="font-size:12px; font-weight:600; color:#34d399;">Administered: ${r.administeredDate}</div>
               <div style="font-size:11px; color:var(--text-dim);">By: ${r.administeredBy || 'Nurse'} | Lot: ${r.batchNumber || 'N/A'}</div>`
            : `<span style="font-size:11px; color:var(--text-dim);">Not yet administered</span>`;

        let actionButtons = '';
        if (currentRole === 'HEALTH_WORKER') {
            actionButtons = `
                <div style="display:flex; gap:6px;">
                    <button class="btn btn-secondary btn-sm" onclick="openAdministerModal(${r.id}, '${escapeQuotes(r.beneficiary ? r.beneficiary.childName : '')}', '${escapeQuotes(r.vaccineName)}', '${r.status}', '${escapeQuotes(r.administeredBy || '')}', '${escapeQuotes(r.batchNumber || '')}')" id="btn-administer-${r.id}">
                        💉 Administer
                    </button>
                    <button class="btn btn-secondary btn-sm" onclick="sendIndividualReminder(${r.id}, '${escapeQuotes(r.beneficiary ? r.beneficiary.childName : '')}', '${escapeQuotes(r.vaccineName)}')">
                        🔔 Alert
                    </button>
                </div>
            `;
        } else {
            actionButtons = `
                <button class="btn btn-secondary btn-sm" onclick="sendIndividualReminder(${r.id}, '${escapeQuotes(r.beneficiary ? r.beneficiary.childName : '')}', '${escapeQuotes(r.vaccineName)}')">
                    📲 Request SMS
                </button>
            `;
        }

        return `
            <tr id="row-record-${r.id}">
                <td style="font-family:var(--font-mono); color:var(--text-dim);">#${r.id}</td>
                <td>
                    <div style="font-weight:700; color:#ffffff;">${r.beneficiary ? r.beneficiary.childName : 'Unknown'}</div>
                    <div style="font-size:11px; color:var(--text-dim);">DOB: ${r.beneficiary ? r.beneficiary.dateOfBirth : 'N/A'}</div>
                </td>
                <td>
                    <div>${r.beneficiary ? r.beneficiary.parentName : 'N/A'}</div>
                    <div style="font-size:11px; font-family:var(--font-mono); color:var(--text-muted);">${r.beneficiary ? r.beneficiary.contactPhone : 'N/A'}</div>
                </td>
                <td>
                    <div style="font-weight:600; color:#e2e8f0;">${r.vaccineName}</div>
                    <div style="font-size:11px; color:var(--text-dim);">${r.targetDisease || ''}</div>
                </td>
                <td style="font-weight:600;">Dose #${r.doseNumber}</td>
                <td style="font-weight:600; color:#38bdf8;">${r.scheduledDate}</td>
                <td><span class="badge ${statusBadgeClass}" id="badge-status-${r.id}">${r.status}</span></td>
                <td>${adminDetails}</td>
                <td>${actionButtons}</td>
            </tr>
        `;
    }).join('');
}

// --- 5. BENEFICIARIES TABLE ---
async function loadBeneficiaries() {
    try {
        const res = await fetch('/api/beneficiaries');
        if (!res.ok) throw new Error('Failed to load beneficiaries');
        const list = await res.json();
        renderBeneficiariesTable(list);
    } catch (err) {
        console.error('Error loading beneficiaries:', err);
    }
}

function renderBeneficiariesTable(list) {
    const tbody = document.getElementById('beneficiaries-table-body');
    if (!tbody) return;

    if (list.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" style="text-align:center; padding:32px; color:var(--text-dim);">No beneficiaries registered yet.</td></tr>`;
        return;
    }

    tbody.innerHTML = list.map(b => `
        <tr id="row-beneficiary-${b.id}">
            <td style="font-family:var(--font-mono); color:var(--text-dim);">#${b.id}</td>
            <td style="font-weight:700; color:#ffffff;">${b.childName}</td>
            <td>${b.dateOfBirth}</td>
            <td><span class="badge badge-gender">${b.gender}</span></td>
            <td>${b.parentName}</td>
            <td style="font-family:var(--font-mono);">${b.contactPhone}</td>
            <td>${b.contactEmail || 'N/A'}</td>
            <td style="font-size:12px; color:var(--text-muted);">${b.address || 'N/A'}</td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="filterByChild('${escapeQuotes(b.childName)}')">
                    🔍 View Schedule
                </button>
            </td>
        </tr>
    `).join('');
}

function filterByChild(name) {
    const navSchedule = document.getElementById('btn-nav-schedule');
    if (navSchedule) navSchedule.click();

    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.value = name;
        loadVaccinationRecords();
    }
}

// --- 6. MODALS & FORMS ---
function initModals() {
    // Open Register Modal
    const openBtn1 = document.getElementById('btn-open-register-modal');
    const openBtn2 = document.getElementById('btn-add-beneficiary-secondary');
    const modalRegister = document.getElementById('modal-register');
    const closeRegister = document.getElementById('modal-register-close');
    const cancelRegister = document.getElementById('btn-cancel-register');

    const openRegister = () => {
        if (modalRegister) modalRegister.classList.add('active');
    };
    const closeRegisterFn = () => {
        if (modalRegister) modalRegister.classList.remove('active');
        document.getElementById('form-register-beneficiary')?.reset();
    };

    if (openBtn1) openBtn1.addEventListener('click', openRegister);
    if (openBtn2) openBtn2.addEventListener('click', openRegister);
    if (closeRegister) closeRegister.addEventListener('click', closeRegisterFn);
    if (cancelRegister) cancelRegister.addEventListener('click', closeRegisterFn);

    // Beneficiary Registration Submit
    const formRegister = document.getElementById('form-register-beneficiary');
    if (formRegister) {
        formRegister.addEventListener('submit', async (e) => {
            e.preventDefault();

            const childName = document.getElementById('childName').value.trim();
            const dateOfBirth = document.getElementById('dateOfBirth').value;
            const gender = document.getElementById('gender').value;
            const parentName = document.getElementById('parentName').value.trim();
            const contactPhone = document.getElementById('contactPhone').value.trim();
            const contactEmail = document.getElementById('contactEmail').value.trim();
            const address = document.getElementById('address').value.trim();

            const payload = { childName, dateOfBirth, gender, parentName, contactPhone, contactEmail, address };

            try {
                const res = await fetch('/api/beneficiaries', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });

                if (res.ok) {
                    const created = await res.json();
                    closeRegisterFn();
                    showToast(`Success! Child ${created.childName} registered with automated schedule.`, 'success');
                    loadDashboardStats();
                    loadVaccinationRecords();
                    loadBeneficiaries();
                } else {
                    const err = await res.json();
                    showToast(`Error: ${err.error || 'Registration failed'}`, 'danger');
                }
            } catch (err) {
                showToast(`Network error: ${err.message}`, 'danger');
            }
        });
    }

    // Administer Modal Handlers
    const modalAdmin = document.getElementById('modal-administer');
    const closeAdmin = document.getElementById('modal-admin-close');
    const cancelAdmin = document.getElementById('btn-cancel-administer');

    const closeAdminFn = () => {
        if (modalAdmin) modalAdmin.classList.remove('active');
    };
    if (closeAdmin) closeAdmin.addEventListener('click', closeAdminFn);
    if (cancelAdmin) cancelAdmin.addEventListener('click', closeAdminFn);

    const formAdmin = document.getElementById('form-administer-vaccine');
    if (formAdmin) {
        formAdmin.addEventListener('submit', async (e) => {
            e.preventDefault();
            const recordId = document.getElementById('admin-record-id').value;
            const status = document.getElementById('admin-status-select').value;
            const administeredBy = document.getElementById('admin-nurse-name').value.trim();
            const batchNumber = document.getElementById('admin-batch-number').value.trim();
            const notes = document.getElementById('admin-notes').value.trim();

            try {
                const res = await fetch(`/api/vaccinations/${recordId}/status`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ status, administeredBy, batchNumber, notes })
                });

                if (res.ok) {
                    closeAdminFn();
                    showToast(`Record #${recordId} updated to ${status}!`, 'success');
                    loadDashboardStats();
                    loadVaccinationRecords();
                } else {
                    showToast('Failed to update status', 'danger');
                }
            } catch (err) {
                showToast(`Error: ${err.message}`, 'danger');
            }
        });
    }
}

window.openAdministerModal = function(recordId, childName, vaccineName, currentStatus, nurse, batch) {
    document.getElementById('admin-record-id').value = recordId;
    document.getElementById('admin-child-name').value = childName;
    document.getElementById('admin-vaccine-name').value = vaccineName;
    document.getElementById('admin-status-select').value = (currentStatus === 'SCHEDULED' || currentStatus === 'OVERDUE') ? 'COMPLETED' : currentStatus;
    document.getElementById('admin-nurse-name').value = nurse || 'Staff Nurse Ananya';
    document.getElementById('admin-batch-number').value = batch || 'LOT-2026-SERUM';

    const modal = document.getElementById('modal-administer');
    if (modal) modal.classList.add('active');
};

// --- 7. SEARCH AND FILTERS ---
function initSearchAndFilter() {
    const searchInput = document.getElementById('search-input');
    const statusFilter = document.getElementById('status-filter');
    const refreshBtn = document.getElementById('btn-refresh-records');

    if (searchInput) {
        let debounceTimer;
        searchInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                loadVaccinationRecords();
            }, 300);
        });
    }

    if (statusFilter) {
        statusFilter.addEventListener('change', () => {
            loadVaccinationRecords();
        });
    }

    if (refreshBtn) {
        refreshBtn.addEventListener('click', () => {
            loadVaccinationRecords();
            loadDashboardStats();
            showToast('Records refreshed', 'info');
        });
    }
}

// --- 8. REMINDERS & NOTIFICATION SIMULATOR ---
function initReminders() {
    const batchBtn1 = document.getElementById('btn-batch-reminders');
    const batchBtn2 = document.getElementById('btn-trigger-batch-sms');
    const clearBtn = document.getElementById('btn-clear-reminder-logs');

    const triggerBatch = async () => {
        try {
            const res = await fetch('/api/reminders/send-batch', { method: 'POST' });
            if (res.ok) {
                const data = await res.json();
                showToast(`Batch SMS broadcast sent to ${data.dispatchedCount} parents!`, 'success');
                logReminderEvent(`Batch SMS alert dispatched to ${data.dispatchedCount} parents for upcoming & overdue doses.`);
                loadDashboardStats();
            }
        } catch (err) {
            showToast('Failed to dispatch batch reminders', 'danger');
        }
    };

    if (batchBtn1) batchBtn1.addEventListener('click', triggerBatch);
    if (batchBtn2) batchBtn2.addEventListener('click', triggerBatch);

    if (clearBtn) {
        clearBtn.addEventListener('click', () => {
            const list = document.getElementById('reminder-log-list');
            if (list) list.innerHTML = `<div style="font-size:12px; color:var(--text-dim); text-align:center; padding:20px;">Logs cleared.</div>`;
        });
    }
}

window.sendIndividualReminder = async function(recordId, childName, vaccineName) {
    try {
        const res = await fetch(`/api/vaccinations/${recordId}/reminder`, { method: 'POST' });
        if (res.ok) {
            showToast(`Simulated SMS Alert sent to parent for ${childName}'s ${vaccineName}!`, 'success');
            logReminderEvent(`Individual SMS Alert sent for Child: ${childName} (Vaccine: ${vaccineName}, Record #${recordId})`);
        }
    } catch (err) {
        showToast('Error sending reminder', 'danger');
    }
};

function logReminderEvent(message) {
    const list = document.getElementById('reminder-log-list');
    if (!list) return;

    const time = new Date().toLocaleTimeString();
    const entry = document.createElement('div');
    entry.style.background = 'rgba(255, 255, 255, 0.04)';
    entry.style.padding = '10px 14px';
    entry.style.borderRadius = '6px';
    entry.style.fontSize = '12px';
    entry.style.borderLeft = '3px solid #0ea5e9';
    entry.innerHTML = `<span style="color:#94a3b8; font-family:var(--font-mono); font-size:10px;">[${time}]</span> <span style="color:#ffffff;">${message}</span>`;

    list.insertBefore(entry, list.firstChild);
}

// --- 9. DEFECT SIMULATION (TASK 10 QUALITY GATE) ---
function initDefectModeToggle() {
    const checkbox = document.getElementById('defect-toggle-checkbox');
    const statusText = document.getElementById('defect-status-text');

    // Fetch initial status
    fetch('/api/admin/defect-mode')
        .then(res => res.json())
        .then(data => {
            if (checkbox) checkbox.checked = data.defectModeEnabled;
            updateDefectStatusUI(data.defectModeEnabled);
        })
        .catch(() => {});

    if (checkbox) {
        checkbox.addEventListener('change', async (e) => {
            const enabled = e.target.checked;
            try {
                const res = await fetch('/api/admin/defect-mode', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ enabled })
                });
                const data = await res.json();
                updateDefectStatusUI(data.defectModeEnabled);
                showToast(data.message, data.defectModeEnabled ? 'danger' : 'success');
            } catch (err) {
                showToast('Failed to update defect mode', 'danger');
            }
        });
    }

    function updateDefectStatusUI(enabled) {
        if (!statusText) return;
        if (enabled) {
            statusText.textContent = 'Status: DEFECT INJECTED (Beneficiary registration will throw 400 Bad Request to simulate Jenkins CI quality gate failure)';
            statusText.style.color = '#fb7185';
        } else {
            statusText.textContent = 'Status: System Normal (Defect Resolved - All Quality Gates Pass)';
            statusText.style.color = '#10b981';
        }
    }
}

// --- UTILITIES ---
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span style="font-size:16px;">${type === 'success' ? '✅' : type === 'danger' ? '⚠️' : 'ℹ️'}</span>
        <div style="flex:1;">${message}</div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

function escapeQuotes(str) {
    if (!str) return '';
    return str.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
