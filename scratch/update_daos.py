import os

# Update PanenDAOImpl.java
with open('src/main/java/dao/PanenDAOImpl.java', 'r', encoding='utf-8') as f:
    c = f.read()

addition_panen = """
    @Override
    public List<Panen> findByBlok(String kodeBlok) throws SQLException {
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql(c) + "WHERE ap.kode_blok_lahan = ? ORDER BY ap.tanggal_panen DESC")) {
            ps.setString(1, kodeBlok);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(PanenMapper.toModel(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Panen> findByKomoditas(String komoditas) throws SQLException {
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql(c) + "WHERE bl.komoditas = ? ORDER BY ap.tanggal_panen DESC")) {
            ps.setString(1, komoditas);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(PanenMapper.toModel(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Panen> findAll(String keyword, String komoditas) throws SQLException {
        if (komoditas == null || "Semua".equalsIgnoreCase(komoditas)) {
            return findAll(keyword);
        }
        List<Panen> list = new ArrayList<>();
        for (Panen p : findAll(keyword)) {
            if (komoditas.equalsIgnoreCase(p.getKomoditas())) {
                list.add(p);
            }
        }
        return list;
    }
}
"""
idx = c.rfind('}')
c = c[:idx] + addition_panen
with open('src/main/java/dao/PanenDAOImpl.java', 'w', encoding='utf-8') as f:
    f.write(c)

# Update EvaluasiDAOImpl.java
with open('src/main/java/dao/EvaluasiDAOImpl.java', 'r', encoding='utf-8') as f:
    e = f.read()

e = e.replace('public class EvaluasiDAOImpl extends BaseDAO<Evaluasi> implements EvaluasiDAO {', 'public class EvaluasiDAOImpl extends EvaluasiDAO {')

addition_eval = """
    @Override
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
    }

    @Override
    public List<Evaluasi> findAll(String keyword, String status) throws SQLException {
        if (status == null || "Semua".equalsIgnoreCase(status)) {
            return findAll(keyword);
        }
        List<Evaluasi> list = new ArrayList<>();
        for (Evaluasi ev : findAll(keyword)) {
            if (status.equalsIgnoreCase(ev.getStatus())) {
                list.add(ev);
            }
        }
        return list;
    }
}
"""
idx_e = e.rfind('}')
e = e[:idx_e] + addition_eval
with open('src/main/java/dao/EvaluasiDAOImpl.java', 'w', encoding='utf-8') as f:
    f.write(e)

print("DAOs updated successfully!")

