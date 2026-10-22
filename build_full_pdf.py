"""
Full PDF Manual Generator using ReportLab
Candidate: Gayatri Patil (PRN: 23102A0016, Roll: 15)
"""
import os
import glob
import re
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, Preformatted, KeepTogether
)
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_decorations(num_pages)
            super().showPage()
        super().save()

    def draw_page_decorations(self, total_pages):
        if self._pageNumber > 1:
            self.saveState()
            self.setFont("Helvetica", 8)
            self.setFillColor(colors.HexColor("#64748b"))
            
            # Running Header
            self.drawString(36, 810, "DevOps Laboratory Manual | Gayatri Patil (PRN: 23102A0016, Roll: 15)")
            self.setStrokeColor(colors.HexColor("#e2e8f0"))
            self.setLineWidth(0.5)
            self.line(36, 804, 559, 804)

            # Running Footer
            page_text = f"Page {self._pageNumber} of {total_pages}"
            self.drawRightString(559, 25, page_text)
            self.drawString(36, 25, "VaxGuard - Vaccination Reminder Portal Selenium Testing Pipeline")
            self.line(36, 35, 559, 35)
            self.restoreState()

def generate_pdf():
    pdf_filename = "DevOps_Lab_Manual_Gayatri_Patil_23102A0016.pdf"
    doc = SimpleDocTemplate(
        pdf_filename,
        pagesize=A4,
        leftMargin=36,
        rightMargin=36,
        topMargin=45,
        bottomMargin=45
    )

    styles = getSampleStyleSheet()

    # Custom Color Palette
    PRIMARY = colors.HexColor("#0284c7")
    DARK = colors.HexColor("#0f172a")
    SECONDARY = colors.HexColor("#4f46e5")
    CODE_BG = colors.HexColor("#0f172a")
    CODE_TEXT = colors.HexColor("#34d399")
    QUOTE_BG = colors.HexColor("#f0f9ff")

    # Custom Typography Styles
    style_cover_dept = ParagraphStyle(
        'CoverDept',
        fontName='Helvetica-Bold',
        fontSize=13,
        leading=16,
        alignment=1, # Center
        textColor=DARK
    )
    style_cover_title = ParagraphStyle(
        'CoverTitle',
        fontName='Helvetica-Bold',
        fontSize=20,
        leading=24,
        alignment=1,
        textColor=PRIMARY
    )
    style_cover_sub = ParagraphStyle(
        'CoverSub',
        fontName='Helvetica-Oblique',
        fontSize=10,
        leading=14,
        alignment=1,
        textColor=colors.HexColor("#475569")
    )
    style_task_h1 = ParagraphStyle(
        'TaskH1',
        fontName='Helvetica-Bold',
        fontSize=14,
        leading=18,
        textColor=PRIMARY,
        spaceBefore=12,
        spaceAfter=6
    )
    style_task_h2 = ParagraphStyle(
        'TaskH2',
        fontName='Helvetica-Bold',
        fontSize=11,
        leading=15,
        textColor=SECONDARY,
        spaceBefore=10,
        spaceAfter=4
    )
    style_task_h3 = ParagraphStyle(
        'TaskH3',
        fontName='Helvetica-Bold',
        fontSize=9.5,
        leading=13,
        textColor=DARK,
        spaceBefore=8,
        spaceAfter=3
    )
    style_body = ParagraphStyle(
        'ReportBody',
        fontName='Helvetica',
        fontSize=8.5,
        leading=11.5,
        textColor=colors.HexColor("#1e293b"),
        spaceAfter=4
    )
    style_speech = ParagraphStyle(
        'SpeechQuote',
        fontName='Helvetica-Oblique',
        fontSize=8.5,
        leading=11.5,
        textColor=colors.HexColor("#0369a1"),
        spaceBefore=4,
        spaceAfter=4
    )
    style_bullet = ParagraphStyle(
        'ReportBullet',
        fontName='Helvetica',
        fontSize=8.5,
        leading=11,
        leftIndent=15,
        textColor=colors.HexColor("#1e293b"),
        spaceAfter=2
    )

    story = []

    # ==========================================
    # 1. COVER PAGE
    # ==========================================
    story.append(Spacer(1, 20))
    story.append(Paragraph("DEPARTMENT OF COMPUTER ENGINEERING", style_cover_dept))
    story.append(Paragraph("<font color='#0284c7'>DEVOPS & CONTINUOUS TESTING LABORATORY</font>", style_cover_dept))
    story.append(Spacer(1, 25))

    story.append(Paragraph("ACADEMIC LABORATORY MANUAL & PROJECT DOSSIER", style_cover_sub))
    story.append(Spacer(1, 8))
    story.append(Paragraph("SELENIUM TESTING PIPELINE FOR A VACCINATION REMINDER PORTAL (VAXGUARD)", style_cover_title))
    story.append(Spacer(1, 8))
    story.append(Paragraph("End-to-End DevOps Automation: Git, Jenkins CI/CD, Docker Containerization & Ansible Provisioning", style_cover_sub))
    story.append(Spacer(1, 35))

    student_table_data = [
        [Paragraph("<b>Candidate Name:</b>", style_body), Paragraph("<b>GAYATRI PATIL</b>", style_body)],
        [Paragraph("<b>Student ID / PRN:</b>", style_body), Paragraph("<font color='#0284c7'><b>23102A0016</b></font>", style_body)],
        [Paragraph("<b>Roll Number:</b>", style_body), Paragraph("<font color='#0284c7'><b>15</b></font>", style_body)],
        [Paragraph("<b>Course / Semester:</b>", style_body), Paragraph("B.Tech / B.E. Computer Engineering (Sem VI)", style_body)],
        [Paragraph("<b>Academic Year:</b>", style_body), Paragraph("2026 &ndash; 2027", style_body)]
    ]
    st_table = Table(student_table_data, colWidths=[150, 280])
    st_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (0,-1), colors.HexColor("#f1f5f9")),
        ('BACKGROUND', (1,0), (1,-1), colors.HexColor("#f8fafc")),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#cbd5e1")),
        ('TOPPADDING', (0,0), (-1,-1), 6),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
        ('RIGHTPADDING', (0,0), (-1,-1), 10),
        ('ALIGN', (0,0), (-1,-1), 'LEFT'),
    ]))
    story.append(st_table)

    story.append(Spacer(1, 40))
    story.append(Paragraph("<font size='8' color='#94a3b8'>Certified under National Immunization Schedule &bull; Software Quality Assurance &bull; DevOps Capstone</font>", style_cover_sub))
    story.append(PageBreak())

    # ==========================================
    # 2. CERTIFICATE PAGE
    # ==========================================
    story.append(Spacer(1, 15))
    story.append(Paragraph("CERTIFICATE OF COMPLETION", style_cover_dept))
    story.append(Spacer(1, 15))

    cert_text = (
        "This is to certify that Miss <b>GAYATRI PATIL</b>, bearing Student ID / PRN <b>23102A0016</b> and Roll Number <b>15</b>, "
        "of Third Year Computer Engineering, has satisfactorily completed all fifteen (15) prescribed laboratory assignments and deliverables for the course of "
        "<b>DevOps Laboratory</b> entitled <i>'Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)'</i> during the academic year 2026&ndash;2027.<br/><br/>"
        "The project work demonstrates proficient mastery of GitFlow branching, automated Selenium WebDriver regression testing, Jenkins continuous integration pipelines, "
        "multi-stage Docker containerization, Ansible idempotent infrastructure provisioning, and automated rollback mechanisms."
    )
    story.append(Paragraph(cert_text, ParagraphStyle('CertText', fontName='Helvetica', fontSize=9.5, leading=15, alignment=4)))
    story.append(Spacer(1, 40))

    sign_table_data = [
        [
            Paragraph("_______________________<br/><b>Internal Examiner</b>", style_cover_sub),
            Paragraph("_______________________<br/><b>External Examiner</b>", style_cover_sub),
            Paragraph("_______________________<br/><b>Head of Department</b>", style_cover_sub)
        ]
    ]
    sign_table = Table(sign_table_data, colWidths=[170, 170, 170])
    sign_table.setStyle(TableStyle([
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
        ('VALIGN', (0,0), (-1,-1), 'BOTTOM'),
    ]))
    story.append(sign_table)
    story.append(PageBreak())

    # ==========================================
    # 3. INDEX TABLE
    # ==========================================
    story.append(Paragraph("INDEX OF 15 DEVOPS LABORATORY ASSIGNMENTS", style_task_h1))
    story.append(Spacer(1, 6))

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

    index_data = [
        [
            Paragraph("<b>Task #</b>", style_body),
            Paragraph("<b>Deliverable Assignment Title</b>", style_body),
            Paragraph("<b>Status</b>", style_body),
            Paragraph("<b>Sign</b>", style_body)
        ]
    ]
    for idx, t in enumerate(task_titles, start=1):
        index_data.append([
            Paragraph(f"<b>Task {idx:02d}</b>", style_body),
            Paragraph(t, style_body),
            Paragraph("Completed", style_body),
            Paragraph("Verified", style_body)
        ])

    idx_table = Table(index_data, colWidths=[55, 310, 75, 75])
    idx_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), PRIMARY),
        ('TEXTCOLOR', (0,0), (-1,0), colors.white),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#e2e8f0")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, colors.HexColor("#f8fafc")]),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
        ('ALIGN', (0,0), (0,-1), 'CENTER'),
        ('ALIGN', (2,0), (-1,-1), 'CENTER'),
    ]))
    story.append(idx_table)

    # ==========================================
    # 4. ALL 15 TASKS (PARSED FROM tasks_for_teacher/)
    # ==========================================
    task_files = sorted(glob.glob("tasks_for_teacher/Task_*.md"))
    for fpath in task_files:
        story.append(PageBreak())
        with open(fpath, "r", encoding="utf-8") as tf:
            lines = tf.readlines()

        in_code = False
        code_lines = []
        in_table = False
        table_rows = []

        for line in lines:
            s = line.strip()

            # Skip redundant student header inside task bodies
            if s.startswith("**Student Name:**") or s.startswith("**PRN / Student ID:**") or s.startswith("**Roll Number:**"):
                continue

            # Code blocks
            if s.startswith("```"):
                if in_code:
                    in_code = False
                    code_text = "\n".join(code_lines)
                    # Use preformatted with wrap or max width
                    pre = Preformatted(
                        code_text,
                        ParagraphStyle('CodeStyle', fontName='Courier', fontSize=7.5, leading=9.5, textColor=CODE_TEXT)
                    )
                    code_box = Table([[pre]], colWidths=[520])
                    code_box.setStyle(TableStyle([
                        ('BACKGROUND', (0,0), (-1,-1), CODE_BG),
                        ('TOPPADDING', (0,0), (-1,-1), 6),
                        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
                        ('LEFTPADDING', (0,0), (-1,-1), 8),
                        ('RIGHTPADDING', (0,0), (-1,-1), 8),
                    ]))
                    story.append(code_box)
                    story.append(Spacer(1, 4))
                    code_lines = []
                else:
                    in_code = True
                continue

            if in_code:
                code_lines.append(line.rstrip('\r\n'))
                continue

            # Markdown Table parsing
            if '|' in s and not s.startswith('#'):
                if not in_table:
                    in_table = True
                    table_rows = []
                if re.match(r'^[\|\s\-:]+$', s):
                    continue
                cells = [c.strip() for c in s.split('|')[1:-1]]
                if cells:
                    table_rows.append(cells)
                continue
            else:
                if in_table:
                    in_table = False
                    if table_rows:
                        # Render Table
                        num_cols = len(table_rows[0])
                        col_w = 520 / num_cols if num_cols > 0 else 520
                        formatted_rows = []
                        for r_idx, r in enumerate(table_rows):
                            row_cells = []
                            for c in r:
                                is_head = (r_idx == 0)
                                cell_p = Paragraph(
                                    f"<b>{c}</b>" if is_head else c,
                                    ParagraphStyle('CellP', fontName='Helvetica', fontSize=7.5, leading=9.5)
                                )
                                row_cells.append(cell_p)
                            formatted_rows.append(row_cells)

                        tbl = Table(formatted_rows, colWidths=[col_w]*num_cols)
                        tbl.setStyle(TableStyle([
                            ('BACKGROUND', (0,0), (-1,0), colors.HexColor("#f1f5f9")),
                            ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#cbd5e1")),
                            ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, colors.HexColor("#f8fafc")]),
                            ('TOPPADDING', (0,0), (-1,-1), 4),
                            ('BOTTOMPADDING', (0,0), (-1,-1), 4),
                            ('LEFTPADDING', (0,0), (-1,-1), 5),
                            ('RIGHTPADDING', (0,0), (-1,-1), 5),
                        ]))
                        story.append(tbl)
                        story.append(Spacer(1, 4))
                    table_rows = []

            if not s:
                continue

            # Headings
            if s.startswith("# "):
                story.append(Paragraph(s[2:].strip(), style_task_h1))
            elif s.startswith("## "):
                story.append(Paragraph(s[3:].strip(), style_task_h2))
            elif s.startswith("### "):
                story.append(Paragraph(s[4:].strip(), style_task_h3))
            elif s.startswith("#### "):
                story.append(Paragraph(f"<b>{s[5:].strip()}</b>", style_body))
            # Blockquotes / Speech
            elif s.startswith(">"):
                q_text = s.lstrip("> ").strip()
                qp = Paragraph(f"<i>&ldquo;{q_text}&rdquo;</i>", style_speech)
                q_box = Table([[qp]], colWidths=[520])
                q_box.setStyle(TableStyle([
                    ('BACKGROUND', (0,0), (-1,-1), QUOTE_BG),
                    ('LINELEFT', (0,0), (0,0), 3, PRIMARY),
                    ('TOPPADDING', (0,0), (-1,-1), 5),
                    ('BOTTOMPADDING', (0,0), (-1,-1), 5),
                    ('LEFTPADDING', (0,0), (-1,-1), 8),
                    ('RIGHTPADDING', (0,0), (-1,-1), 8),
                ]))
                story.append(q_box)
                story.append(Spacer(1, 3))
            # Bullets
            elif s.startswith("- ") or s.startswith("* "):
                b_text = s[2:].strip()
                b_text = re.sub(r'\*\*(.*?)\*\*', r'<b>\1</b>', b_text)
                b_text = re.sub(r'`(.*?)`', r'<font face="Courier">\1</font>', b_text)
                story.append(Paragraph(f"&bull; {b_text}", style_bullet))
            else:
                p_text = re.sub(r'\*\*(.*?)\*\*', r'<b>\1</b>', s)
                p_text = re.sub(r'`(.*?)`', r'<font face="Courier">\1</font>', p_text)
                story.append(Paragraph(p_text, style_body))

        if in_table and table_rows:
            num_cols = len(table_rows[0])
            col_w = 520 / num_cols if num_cols > 0 else 520
            formatted_rows = []
            for r_idx, r in enumerate(table_rows):
                row_cells = []
                for c in r:
                    is_head = (r_idx == 0)
                    cell_p = Paragraph(f"<b>{c}</b>" if is_head else c, ParagraphStyle('CellP', fontName='Helvetica', fontSize=7.5, leading=9.5))
                    row_cells.append(cell_p)
                formatted_rows.append(row_cells)
            tbl = Table(formatted_rows, colWidths=[col_w]*num_cols)
            tbl.setStyle(TableStyle([
                ('BACKGROUND', (0,0), (-1,0), colors.HexColor("#f1f5f9")),
                ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#cbd5e1")),
                ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, colors.HexColor("#f8fafc")]),
                ('TOPPADDING', (0,0), (-1,-1), 4),
                ('BOTTOMPADDING', (0,0), (-1,-1), 4),
            ]))
            story.append(tbl)

    # Build document
    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"[SUCCESS] Native ReportLab PDF generated: {pdf_filename} ({os.path.getsize(pdf_filename)} bytes)")

if __name__ == "__main__":
    generate_pdf()
