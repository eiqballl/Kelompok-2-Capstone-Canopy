with open('src/main/java/model/DataStore.java', 'r', encoding='utf-8') as f:
    text = f.read()

old_p_id = """    private String produkId(String idPanen) {
        String digits = idPanen == null ? "" : idPanen.replaceAll("\\\\D+", "");
        if (digits.isEmpty()) digits = "0";
        return String.format("PR%03d", Integer.parseInt(digits));
    }"""

new_p_id = """    private String produkId(String idPanen) {
        String digits = idPanen == null ? "" : idPanen.replaceAll("\\\\D+", "");
        if (digits.isEmpty()) digits = "0";
        try {
            return String.format("PR%03d", Integer.parseInt(digits));
        } catch (Exception e) {
            return String.format("PR%03d", Math.abs(idPanen.hashCode()) % 1000);
        }
    }"""

text = text.replace(old_p_id, new_p_id)
with open('src/main/java/model/DataStore.java', 'w', encoding='utf-8') as f:
    f.write(text)

print("DataStore produkId patched!")

