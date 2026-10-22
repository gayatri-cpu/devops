"""
Script to generate high-resolution, print-ready HTML and compile to PDF via Edge Headless
Candidate: Gayatri Patil (PRN: 23102A0016, Roll: 16)
"""
import os
import glob
import re
import subprocess

def markdown_to_html(md_text):
    html = []
    lines = md_text.split('\n')
    in_code = False
    code_lang = ""
    code_lines = []
    in_table = False
    table_rows = []

    for line in lines:
        s = line.strip()

        # Code block
        if s.startswith('```'):
            if in_code:
                in_code = False
                code_content = "\n".join(code_lines).replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
                html.append(f'<pre class="code-block"><code>{code_content}</code></pre>')
                code_lines = []
            else:
                in_code = True
                code_lang = s[3:].strip()
            continue

        if in_code:
            code_lines.append(line)
            continue

        # Tables
        if '|' in s and not s.startswith('#'):
            if not in_table:
                in_table = True
                table_rows = []
            if re.match(r'^[\|\s\-:]+$', s):
                continue # delimiter row
            cells = [c.strip() for c in s.split('|')[1:-1]]
            if cells:
                table_rows.append(cells)
            continue
        else:
            if in_table:
                in_table = False
                if table_rows:
                    t_html = ['<table class="report-table">']
                    # Header
                    t_html.append('<thead><tr>')
                    for c in table_rows[0]:
                        t_html.append(f'<th>{c}</th>')
                    t_html.append('</tr></thead><tbody>')
                    for r in table_rows[1:]:
                        t_html.append('<tr>')
                        for c in r:
                            t_html.append(f'<td>{c}</td>')
                        t_html.append('</tr>')
                    t_html.append('</tbody></table>')
                    html.append('\n'.join(t_html))
                table_rows = []

        if not s:
            continue

        # Headings
        if s.startswith('# '):
            html.append(f'<h1 class="task-h1">{s[2:].strip()}</h1>')
        elif s.startswith('## '):
            html.append(f'<h2 class="task-h2">{s[3:].strip()}</h2>')
        elif s.startswith('### '):
            html.append(f'<h3 class="task-h3">{s[4:].strip()}</h3>')
        elif s.startswith('#### '):
            html.append(f'<h4 class="task-h4">{s[5:].strip()}</h4>')
        # Blockquotes
        elif s.startswith('>'):
            q_text = s.lstrip('> ').strip()
            html.append(f'<div class="speech-quote">{q_text}</div>')
        # Bullets
        elif s.startswith('- ') or s.startswith('* '):
            b_text = s[2:].strip()
            # bold tags
            b_text = re.sub(r'\*\*(.*?)\*\*', r'<strong>\1</strong>', b_text)
            b_text = re.sub(r'`(.*?)`', r'<code>\1</code>', b_text)
            html.append(f'<li class="bullet-item">{b_text}</li>')
        else:
            p_text = re.sub(r'\*\*(.*?)\*\*', r'<strong>\1</strong>', s)
            p_text = re.sub(r'`(.*?)`', r'<code>\1</code>', p_text)
            html.append(f'<p class="report-p">{p_text}</p>')

    if in_table and table_rows:
        t_html = ['<table class="report-table"><thead><tr>']
        for c in table_rows[0]:
            t_html.append(f'<th>{c}</th>')
        t_html.append('</tr></thead><tbody>')
        for r in table_rows[1:]:
            t_html.append('<tr>')
            for c in r:
                t_html.append(f'<td>{c}</td>')
            t_html.append('</tr>')
        t_html.append('</tbody></table>')
        html.append('\n'.join(t_html))

    return '\n'.join(html)

def build_complete_html():
    css = """
    @page {
        size: A4;
        margin: 18mm 16mm 18mm 16mm;
        @bottom-right {
            content: "Page " counter(page);
            font-size: 9pt;
            color: #64748b;
        }
    }
    body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        color: #1e293b;
        line-height: 1.5;
        background: #ffffff;
        margin: 0;
        padding: 0;
        font-size: 10pt;
    }
    .page-break {
        page-break-before: always;
    }
    .cover-container {
        text-align: center;
        padding: 40px 20px;
        min-height: 90vh;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
    .cover-header {
        border-bottom: 3px double #0ea5e9;
        padding-bottom: 20px;
        margin-bottom: 40px;
    }
    .univ-name {
        font-size: 16pt;
        font-weight: 800;
        color: #0f172a;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        margin-bottom: 6px;
    }
    .dept-name {
        font-size: 12pt;
        font-weight: 700;
        color: #0ea5e9;
        text-transform: uppercase;
        letter-spacing: 0.08em;
    }
    .course-title {
        font-size: 11pt;
        font-weight: 600;
        color: #64748b;
        margin-top: 4px;
    }
    .proj-title-box {
        background: #f8fafc;
        border: 2px solid #e2e8f0;
        border-radius: 12px;
        padding: 30px 20px;
        margin: 40px 0;
    }
    .proj-badge {
        display: inline-block;
        background: #0ea5e9;
        color: white;
        font-size: 9pt;
        font-weight: 700;
        padding: 4px 14px;
        border-radius: 20px;
        text-transform: uppercase;
        margin-bottom: 12px;
    }
    .proj-title {
        font-size: 20pt;
        font-weight: 800;
        color: #0f172a;
        line-height: 1.3;
        margin-bottom: 10px;
    }
    .proj-subtitle {
        font-size: 11pt;
        color: #475569;
        font-style: italic;
    }
    .student-card {
        margin: 30px auto;
        width: 80%;
        border-collapse: collapse;
    }
    .student-card td {
        padding: 8px 14px;
        border: 1px solid #cbd5e1;
        font-size: 10.5pt;
    }
    .student-card td.lbl {
        background: #f1f5f9;
        font-weight: 700;
        width: 35%;
        color: #334155;
    }
    .student-card td.val {
        font-weight: 600;
        color: #0f172a;
    }
    .cert-container {
        padding: 30px 20px;
    }
    .cert-heading {
        text-align: center;
        font-size: 18pt;
        font-weight: 800;
        color: #0f172a;
        margin-bottom: 24px;
        text-transform: uppercase;
        letter-spacing: 0.05em;
    }
    .cert-body {
        font-size: 11pt;
        line-height: 1.8;
        text-align: justify;
        margin-bottom: 40px;
    }
    .sign-table {
        width: 100%;
        margin-top: 60px;
        border: none;
    }
    .sign-table td {
        border: none;
        text-align: center;
        padding: 10px;
        vertical-align: bottom;
    }
    .sign-line {
        border-top: 1px solid #334155;
        width: 80%;
        margin: 0 auto;
        padding-top: 6px;
        font-weight: 700;
        font-size: 10pt;
    }
    .index-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
    }
    .index-table th {
        background: #0ea5e9;
        color: white;
        padding: 8px 10px;
        font-size: 9.5pt;
        font-weight: 700;
        border: 1px solid #0284c7;
        text-align: left;
    }
    .index-table td {
        padding: 7px 10px;
        border: 1px solid #e2e8f0;
        font-size: 9pt;
    }
    .index-table tr:nth-child(even) td {
        background: #f8fafc;
    }
    .task-h1 {
        font-size: 15pt;
        font-weight: 800;
        color: #0284c7;
        border-bottom: 2px solid #e2e8f0;
        padding-bottom: 4px;
        margin-top: 20px;
        margin-bottom: 10px;
    }
    .task-h2 {
        font-size: 12pt;
        font-weight: 700;
        color: #1e3a8a;
        margin-top: 14px;
        margin-bottom: 6px;
    }
    .task-h3 {
        font-size: 10.5pt;
        font-weight: 700;
        color: #0f172a;
        margin-top: 10px;
        margin-bottom: 4px;
    }
    .report-p {
        margin: 4px 0 8px 0;
        text-align: justify;
    }
    .speech-quote {
        background: #f0f9ff;
        border-left: 4px solid #0ea5e9;
        padding: 8px 14px;
        margin: 8px 0;
        font-style: italic;
        color: #0369a1;
        font-size: 9.5pt;
    }
    .code-block {
        background: #0f172a;
        color: #34d399;
        padding: 10px 14px;
        border-radius: 6px;
        font-family: "Consolas", "Courier New", monospace;
        font-size: 8.5pt;
        overflow-x: auto;
        margin: 8px 0;
        line-height: 1.4;
    }
    .report-table {
        width: 100%;
        border-collapse: collapse;
        margin: 10px 0;
    }
    .report-table th {
        background: #f1f5f9;
        color: #334155;
        font-weight: 700;
        padding: 6px 10px;
        border: 1px solid #cbd5e1;
        font-size: 9pt;
        text-align: left;
    }
    .report-table td {
        padding: 6px 10px;
        border: 1px solid #cbd5e1;
        font-size: 8.5pt;
    }
    .report-table tr:nth-child(even) td {
        background: #f8fafc;
    }
    code {
        background: #f1f5f9;
        color: #0f172a;
        font-family: "Consolas", monospace;
        font-size: 9pt;
        padding: 2px 5px;
        border-radius: 4px;
    }
    .bullet-item {
        margin-left: 18px;
        margin-bottom: 3px;
    }
    """

    task_titles = [
        "Problem Definition, Stakeholder Analysis and Frozen Scope",
        "Agile Planning, User Stories, Product Backlog and DevOps Workflow",
        "Requirements, Multi-Tier Architecture and Spring Boot Tech Setup",
        "Git and GitHub Repository Initialization, .gitignore and Branch Policy",
        "Feature Development with Branching: National Immunization Schedule Engine",
        "MVP Completion, Simulated Merge Conflict Resolution and Release Tag v1.0.0",
        "Jenkins Installation, SCM Polling Trigger and Continuous Integration Job",
        "Pipeline as Code: Declarative 8-Stage Jenkinsfile with Environment Parameters",
        "Selenium Automated Test Design: 5 Critical User Journeys and Screenshot Listener",
        "Continuous Testing in Jenkins: Quality Gate Enforcement, Defect Fix & Rerun",
        "Docker Multi-Stage Image Build, Non-Root Hardening and Container Lifecycle",
        "Jenkins-Docker Continuous Deployment: Container Rollout and Health Gate",
        "Configuration Management Script: Server Prerequisites & Ansible Playbook",
        "Automated Provisioning, Idempotency Validation (changed=0) and Fast Rollback",
        "Final Release Release Dossier, Live Demonstration Guide and Complete Viva Prep"
    ]

    index_rows = ""
    for idx, t in enumerate(task_titles, start=1):
        index_rows += f"""
        <tr>
            <td style="text-align:center; font-weight:bold;">Task {idx:02d}</td>
            <td>{t}</td>
            <td style="text-align:center;">Completed</td>
            <td style="text-align:center;">Verified</td>
        </tr>
        """

    tasks_html = []
    task_files = sorted(glob.glob("tasks_for_teacher/Task_*.md"))
    for fpath in task_files:
        with open(fpath, "r", encoding="utf-8") as tf:
            md_content = tf.read()
        rendered = markdown_to_html(md_content)
        tasks_html.append(f'<div class="page-break"></div><div class="task-section">{rendered}</div>')

    full_html = f"""<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>DevOps Lab Manual - Gayatri Patil (23102A0016)</title>
    <style>{css}</style>
</head>
<body>

    <!-- 1. COVER PAGE -->
    <div class="cover-container">
        <div class="cover-header">
            <div class="univ-name">DEPARTMENT OF COMPUTER ENGINEERING</div>
            <div class="dept-name">DevOps &amp; Continuous Testing Laboratory</div>
            <div class="course-title">Bachelor of Technology / Bachelor of Engineering &bull; Semester VI</div>
        </div>

        <div class="proj-title-box">
            <div class="proj-badge">Laboratory Project Manual</div>
            <div class="proj-title">Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)</div>
            <div class="proj-subtitle">Comprehensive DevOps Lifecycle: Git, Jenkins CI/CD, Docker Containerization &amp; Ansible Provisioning</div>
        </div>

        <table class="student-card">
            <tr>
                <td class="lbl">Student Name:</td>
                <td class="val">GAYATRI PATIL</td>
            </tr>
            <tr>
                <td class="lbl">Student ID / PRN:</td>
                <td class="val" style="color: #0284c7;">23102A0016</td>
            </tr>
            <tr>
                <td class="lbl">Roll Number:</td>
                <td class="val" style="color: #0284c7;">15</td>
            </tr>
            <tr>
                <td class="lbl">Class / Batch:</td>
                <td class="val">TE Computer Engineering / Batch B</td>
            </tr>
            <tr>
                <td class="lbl">Academic Year:</td>
                <td class="val">2026 &ndash; 2027</td>
            </tr>
        </table>

        <div style="font-size: 9pt; color: #94a3b8; margin-top: 30px;">
            Certified under National Immunization Schedule &bull; Software Quality &amp; Automated Testing Laboratory
        </div>
    </div>

    <!-- 2. CERTIFICATE -->
    <div class="page-break"></div>
    <div class="cert-container">
        <div class="cert-heading">Certificate of Completion</div>
        <p class="cert-body">
            This is to certify that Miss <strong>GAYATRI PATIL</strong>, bearing Student ID / PRN <strong>23102A0016</strong> and Roll Number <strong>16</strong>, 
            of Third Year Computer Engineering, has satisfactorily completed all fifteen (15) prescribed laboratory assignments and deliverables for the course of 
            <strong>DevOps Laboratory</strong> entitled <em>'Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)'</em> during the academic year 2026&ndash;2027.
        </p>

        <p class="cert-body">
            The project work encompasses GitFlow branching, automated Selenium WebDriver regression testing, Jenkins continuous integration pipelines, multi-stage Docker containerization, 
            Ansible idempotent infrastructure provisioning, and automated rollback mechanisms.
        </p>

        <table class="sign-table">
            <tr>
                <td style="width: 33%;">
                    <div class="sign-line">Internal Examiner</div>
                </td>
                <td style="width: 33%;">
                    <div class="sign-line">External Examiner</div>
                </td>
                <td style="width: 33%;">
                    <div class="sign-line">Head of Department</div>
                </td>
            </tr>
        </table>
    </div>

    <!-- 3. TABLE OF CONTENTS -->
    <div class="page-break"></div>
    <div class="cert-container">
        <h2 style="font-size: 15pt; color: #0284c7; border-bottom: 2px solid #e2e8f0; padding-bottom: 6px;">
            Index of 15 DevOps Laboratory Assignments
        </h2>
        <table class="index-table">
            <thead>
                <tr>
                    <th style="width: 12%; text-align:center;">Task #</th>
                    <th style="width: 58%;">Deliverable Assignment Title</th>
                    <th style="width: 15%; text-align:center;">Status</th>
                    <th style="width: 15%; text-align:center;">Verification</th>
                </tr>
            </thead>
            <tbody>
                {index_rows}
            </tbody>
        </table>
    </div>

    <!-- 4. ALL 15 TASKS -->
    {''.join(tasks_html)}

</body>
</html>
    """

    out_html = "DevOps_Lab_Manual_Gayatri_Patil_23102A0016.html"
    with open(out_html, "w", encoding="utf-8") as hf:
        hf.write(full_html)
    print(f"[SUCCESS] HTML manual written: {out_html}")

    # Compile to PDF using Edge Headless
    edge_bin = r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"
    out_pdf = "DevOps_Lab_Manual_Gayatri_Patil_23102A0016.pdf"
    abs_html = os.path.abspath(out_html)

    cmd = [
        edge_bin,
        "--headless",
        "--disable-gpu",
        "--run-all-compositor-stages-before-draw",
        "--no-pdf-header-footer",
        f"--print-to-pdf={out_pdf}",
        f"file:///{abs_html}"
    ]

    print("[INFO] Invoking Microsoft Edge to compile PDF...")
    res = subprocess.run(cmd, capture_output=True, text=True)
    if os.path.exists(out_pdf) and os.path.getsize(out_pdf) > 0:
        print(f"[SUCCESS] PDF Generated successfully: {out_pdf} ({os.path.getsize(out_pdf)} bytes)")
    else:
        print(f"[ERROR] PDF generation failed: {res.stderr}")

if __name__ == "__main__":
    build_complete_html()
