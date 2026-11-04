import glob
import re

for path in glob.glob('src/main/java/view/*.java'):
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find JOptionPane.showMessageDialog calls
    matches = re.findall(r'JOptionPane\.showMessageDialog\([^;]+;', content)
    long_plain = []
    for m in matches:
        # Check if it has a long string literal without <html>
        str_matches = re.findall(r'"([^"\\]*(?:\\.[^"\\]*)*)"', m)
        for s in str_matches:
            if len(s) > 40 and not s.startswith('<html>') and not s.startswith('SELECT') and not s.startswith('INSERT'):
                long_plain.append(s)

    if long_plain:
        print(f"File {path}: {len(long_plain)} long plain string popups found:")
        for s in long_plain[:5]:
            print("  -", s[:60])

