"""
Script to update Roll Number from 15 to 16 across all source files, documentation, HTML, and scripts.
"""
import os
import re

EXTENSIONS = ['.md', '.html', '.java', '.xml', '.py', '.txt', '.properties', '.yml', '.yaml', '.sh', '.ps1', '.ini', '.cfg', 'Dockerfile', 'Jenkinsfile']

patterns = [
    (re.compile(r'\*\*Roll Number:\*\*\s*15', re.IGNORECASE), '**Roll Number:** 16'),
    (re.compile(r'\*\*Roll Number:\*\*\s*`15`', re.IGNORECASE), '**Roll Number:** `16`'),
    (re.compile(r'\*\*Roll No:\*\*\s*15', re.IGNORECASE), '**Roll No:** 16'),
    (re.compile(r'\*\*Roll No:\*\*\s*`15`', re.IGNORECASE), '**Roll No:** `16`'),
    (re.compile(r'Roll Number:\s*15', re.IGNORECASE), 'Roll Number: 16'),
    (re.compile(r'Roll Number:\s*`15`', re.IGNORECASE), 'Roll Number: `16`'),
    (re.compile(r'Roll No:\s*15', re.IGNORECASE), 'Roll No: 16'),
    (re.compile(r'Roll No\.:\s*15', re.IGNORECASE), 'Roll No.: 16'),
    (re.compile(r'Roll:\s*15\b', re.IGNORECASE), 'Roll: 16'),
    (re.compile(r'Roll\s+15\b', re.IGNORECASE), 'Roll 16'),
    (re.compile(r'Roll:\s*`15`', re.IGNORECASE), 'Roll: `16`'),
    (re.compile(r'Roll Number 15\b', re.IGNORECASE), 'Roll Number 16'),
    (re.compile(r'Roll Number\s*<strong>15</strong>', re.IGNORECASE), 'Roll Number <strong>16</strong>'),
    (re.compile(r'Roll Number\s*<b>15</b>', re.IGNORECASE), 'Roll Number <b>16</b>'),
    (re.compile(r'\("Roll Number:",\s*"15"\)'), '("Roll Number:", "16")'),
    (re.compile(r'<strong>Roll Number:</strong>\s*15'), '<strong>Roll Number:</strong> 16'),
    (re.compile(r'<font color=[\'"]#0284c7[\'"]><b>15</b></font>'), '<font color=\'#0284c7\'><b>16</b></font>'),
    (re.compile(r'<b>15</b>', re.IGNORECASE), '<b>16</b>'),
]

count = 0
modified_files = []

for root, dirs, files in os.walk('.'):
    if '.git' in root or 'target' in root or '.tools' in root or 'node_modules' in root:
        continue
    for file in files:
        if any(file.endswith(ext) or file == ext for ext in EXTENSIONS):
            fpath = os.path.join(root, file)
            if file == 'update_roll_number.py':
                continue
            try:
                with open(fpath, 'r', encoding='utf-8') as f:
                    content = f.read()
                new_content = content
                for pattern, repl in patterns:
                    new_content = pattern.sub(repl, new_content)
                if new_content != content:
                    with open(fpath, 'w', encoding='utf-8') as f:
                        f.write(new_content)
                    modified_files.append(fpath)
                    count += 1
            except Exception as e:
                print(f"Error processing {fpath}: {e}")

print(f"[SUCCESS] Updated Roll Number in {count} files:")
for mf in modified_files:
    print(f" - {mf}")
