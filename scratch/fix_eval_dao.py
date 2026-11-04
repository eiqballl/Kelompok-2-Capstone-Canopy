with open('src/main/java/dao/EvaluasiDAOImpl.java', 'r', encoding='utf-8') as f:
    text = f.read()

old_part = """    @Override
    public List<Evaluasi> findByStatus(String status) throws SQLException {
        List<Evaluasi> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql() + " WHERE status_evaluasi = ? ORDER BY tanggal_akhir DESC")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(EvaluasiMapper.toModel(rs));
                }
            }
        }
        return list;
    }"""

new_part = """    @Override
    public List<Evaluasi> findByStatus(String status) throws SQLException {
        List<Evaluasi> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE ep.status = ? ORDER BY ep.tanggal_evaluasi DESC, ep.id_evaluasi DESC")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(EvaluasiMapper.toModel(rs));
                }
            }
        }
        return list;
    }"""

text = text.replace(old_part, new_part)
with open('src/main/java/dao/EvaluasiDAOImpl.java', 'w', encoding='utf-8') as f:
    f.write(text)
print("Fixed EvaluasiDAOImpl!")

