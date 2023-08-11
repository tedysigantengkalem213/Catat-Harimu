package com.teddy.catatharimu;

// Eddy Rochman
// 10120052
// IF-2
public class CatatanModel {
    public String id;
    public String tanggal;
    public String waktu;
    public String judul;
    public String isi;
    public String kategori;

    public CatatanModel() {
        // Default constructor required for Firebase
    }

    public CatatanModel(String id, String tanggal, String waktu, String judul, String isi, String kategori) {
        this.id = id;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.judul = judul;
        this.isi = isi;
        this.kategori = kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getKategori() {
        return kategori;
    }
}
