package model;

public class Pegawai {
    private String idPegawai;
    private String namaPegawai;
    private String username;
    private String password;
    private String noTelp;
    private String role;
    private String detailRole;

    public Pegawai(String idPegawai, String namaPegawai, String username, String password, String noTelp, String role) {
        this(idPegawai, namaPegawai, username, password, noTelp, role, "");
    }

    public Pegawai(String idPegawai, String namaPegawai, String username, String password, String noTelp, String role, String detailRole) {
        this.idPegawai = idPegawai;
        this.namaPegawai = namaPegawai;
        this.username = username;
        this.password = password;
        this.noTelp = noTelp;
        this.role = role;
        this.detailRole = detailRole == null ? "" : detailRole;
    }

    public String getIdPegawai() { return idPegawai; }
    public void setIdPegawai(String idPegawai) { this.idPegawai = idPegawai; }

    public String getNamaPegawai() { return namaPegawai; }
    public void setNamaPegawai(String namaPegawai) { this.namaPegawai = namaPegawai; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDetailRole() { return detailRole; }
    public void setDetailRole(String detailRole) { this.detailRole = detailRole == null ? "" : detailRole; }
}
