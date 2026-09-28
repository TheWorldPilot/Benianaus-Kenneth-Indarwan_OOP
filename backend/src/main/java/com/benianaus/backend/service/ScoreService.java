package com.benianaus.backend.service;

import com.benianaus.backend.model.Score;
import com.benianaus.backend.repository.ScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Tambahkan anotasi yang membuat Spring mengenali class ini sebagai service layer
@Service
public class ScoreService {
    // Tambahkan anotasi untuk melakukan Dependency Injection dari Instance yang sudah ada (ScoreRepository)
    @Autowired
    private ScoreRepository scoreRepository;
    // Tambahkan private field untuk ScoreRepository

    // buat public method createScore yang dapat menerima parameter Score dan mengembalikan score yang telah dibuat
    // hint: gunakan scoreRepository untuk menyimpan score baru ke database

    public Score createScore(Score score){
        return scoreRepository.save(score);
    }

    // buat public method getScoreByID yang dapat menerima parameter UUID dan mengembalikan score sesuai dengan scoreId yang diberikan dengan parameter
    // hint: gunakan Optional<Score> untuk menangani kemungkinan score tersebut ditemukan atau tidak di database
    // hint: gunakan scoreRepository untuk mencari score berdasarkan scoreId
    public Optional<Score> getScoreByID(UUID id){
        return scoreRepository.findById(id);
    }

    public List<Score> getAllScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database kemudian kembalikan hasilnya
        // hint: Panggil method yang sama seperti kode yang kalian buat di TP nomor 4
        return scoreRepository.findAll();
    }

    public List<Score> getRecentScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database dengan urutan pembuatan terbaru kemudian kembalikan hasilnya
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database yang memiliki point di atas nilai tertentu
        // gunakan minValue sebagai batas bawah nilai point
        return scoreRepository.findByPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        // TODO: Gunakan scoreRepository untuk mencari Top Scores dan berikan parameter yang sesuai
        return scoreRepository.findTopScores(limit);
    }

    public void deleteScore(UUID scoreId) {
        // TODO:
        // 1. Cari score yang ingin dihapus menggunakan scoreRepository kemudian simpan score tersebut (hint: lihat caranya di getScoreById())
        // 2. Cek apakah score tersebut ditemukan atau tidak dengan `.orElseThrow(()-> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan"));`
        // 3. Panggil delete() dari scoreRepository untuk menghapus score yang disimpan tadi
        scoreRepository.delete(getScoreByID(scoreId).orElseThrow(()-> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan")));
    }


}