package com.benianaus.backend.controller;

import com.benianaus.backend.model.Score;
import com.benianaus.backend.repository.ScoreRepository;
import com.benianaus.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {
    // Tambahkan anotasi untuk melakukan Dependency Injection dari Instance yang sudah ada (ScoreService
    // Tambahkan private field untuk ScoreService
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private ScoreRepository scoreRepository;

    // GET /api/scores/{scoreId}
    // tambahkan anotasi untuk maps HTTP GET ke method ini dengan path "/{scoreId}"
    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {
        // buat variabel score untuk menyimpan score yang didapat dari scoreService
        // hint: gunakan tipe data Optional
        // hint: gunakan method getScoreById dari scoreService dengan parameter yang sesuai
        Optional score = scoreService.getScoreByID(scoreId);

        // cek apakah variabel score ada isinya dengan isPresent()
        // jika ya kembalikan score yang dipost (hint: kembalikan `ResponseEntity.ok(score.get())`)
        // jika tidak maka kembalikan status NOT_FOUND dengan keterangan body error yang sesuai
        if (score.isPresent()){
            return ResponseEntity.ok(score.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score Kosong");
        }
    }

    //POST /api/scores
    // Tambahkan anotsi untuk maps HTTP POST ke method ini
    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score){
        try{
            // Buat instance score baru menggunakakan scoreService dengan data yang ada dari parameter
            // kembalikan response data score baru dengan status CREATED
            Score newScore = scoreService.createScore(score);
            return ResponseEntity.status(HttpStatus.CREATED).body(newScore);
        } catch (RuntimeException e){
            // kembalikan response error dengan status BAD_REQUEST beserta body error yang sesuai
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hmm, error iki mas e");
        }
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/{scoreId}")
    public ResponseEntity<List<Score>> getAllScores() {
        // 2. Gunakan scoreService untuk memanggil getAllScores() dan simpan scores tersebut ke suatu variabel menggunakan List
        // 3. Kembalikan variabel berisi scores tersebut
        List<Score> newScoreList = scoreService.getAllScores();
        return ResponseEntity.status(HttpStatus.OK).body(newScoreList);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/{scoreId}")
    public ResponseEntity<List<Score>> getLeaderboardByPoint(@RequestParam(defaultValue = "10") Integer limit){
        // 4. Gunakan scoreService untuk memanggil getLeaderboard() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        // 5. Kembalikan variabel berisi scores tersebut
        List<Score> newLeaderboardList = scoreService.getLeaderboard(limit);
        return ResponseEntity.status(HttpStatus.OK).body(newLeaderboardList);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/{scoreId}")
    public ResponseEntity<List<Score>> getScoresAboveValue(@PathVariable Integer minValue){
        // 3. Gunakan scoreService untuk memanggil getScoreAboveValue() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        // 4. Kembalikan variabel berisi scores tersebut
        List<Score> listOfScoreAboveValue = scoreService.getScoreAboveValue(minValue);
        return ResponseEntity.status(HttpStatus.OK).body(listOfScoreAboveValue);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/{scoreId}")
    public ResponseEntity<List<Score>> getRecentScores(){
        // 2. Gunakan scoreService untuk memanggil getRecentScores() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        // 3. Kembalikan variabel berisi scores tersebut
        List<Score> recentScores = scoreService.getRecentScores();
        return ResponseEntity.status(HttpStatus.OK).body(recentScores);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint DELETE beserta endpoint yang sesuai
    @DeleteMapping
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId){
        // 3. buat try-catch block
        // pada try block:
        //  gunakan scoreService untuk memanggil deleteScore() dengan parameter yang sesuai
        //  kembalikan respons untuk menandakan score berhasil dihapus
        // pada catch block:
        //  kembalikan response error dengan status NOT_FOUND beserta body error yang sesuai
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.status(HttpStatus.OK).body("ID "+scoreId+" succesfully deleted");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID "+scoreId+" not found");
        }
    }




}
