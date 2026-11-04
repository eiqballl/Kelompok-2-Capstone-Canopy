with open('src/main/java/model/DataStore.java', 'r', encoding='utf-8') as f:
    text = f.read()

text = text.replace(
    'return String.format("Blok: %s | Tgl: %s | Komoditas: %s | Total: %.1f Kg | Reject: %.1f Kg | Mutu: %s",',
    'return String.format(Locale.US, "Blok: %s | Tgl: %s | Komoditas: %s | Total: %.1f Kg | Reject: %.1f Kg | Mutu: %s",'
)

with open('src/main/java/model/DataStore.java', 'w', encoding='utf-8') as f:
    f.write(text)

print("Updated Locale in DataStore.java!")

