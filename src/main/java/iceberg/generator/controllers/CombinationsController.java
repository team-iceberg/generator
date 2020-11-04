package iceberg.generator.controllers;

import iceberg.generator.models.Combination;
import iceberg.generator.services.GraduationService;
import iceberg.generator.services.impl.CustomExcelWriter;
import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller to manage membership
 */
@RestController
@RequestMapping("/combination")
public class CombinationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CombinationsController.class);

    @Autowired
    private GraduationService graduationService;

    @PostMapping(value = "/file")
    public ResponseEntity getCombination(@RequestPart("file") MultipartFile file) {
        try {
            Map<String, Integer> graduations = graduationService.getGraduations(file.getInputStream());

            List<Combination> optimizeCombination = new ArrayList<>();

            while (graduations.size() > 0) {
                Combination smallestCombination = getSmallest(graduations);
                if (smallestCombination != null) {
                    smallestCombination.getGraduates().forEach(graduations::remove);
                    optimizeCombination.add(smallestCombination);
                } else {
                    graduations = new HashMap<>();
                }
            }
            Collections.sort(optimizeCombination, Comparator.comparing(Combination::getDifferentWith3000));
            CustomExcelWriter customExcelWriter = new CustomExcelWriter();
            customExcelWriter.createXLSXFile(optimizeCombination);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Combination getSmallest(Map<String, Integer> graduations) {
        List<Combination> combinations = Generator.combination(graduations.keySet().toArray()).simple(3).stream().map(graduates -> {
            final String[] combination = { "" };
            final Integer[] totalLength = { 0 };
            graduates.stream().forEach(graduate -> {
                if (combination[0].isEmpty()) {
                    combination[0] = combination[0] + graduate;
                } else {
                    combination[0] = combination[0] + " - " + graduate;
                }
                if(graduate==null){
                    Integer t = 0;
                }
                totalLength[0] = totalLength[0] + graduations.get(graduate);
            });
            return Combination.builder().graduates(graduates).combination(combination[0]).totalLength(totalLength[0])
                    .differentWith3000(totalLength[0] - 3000).build();
        }).collect(Collectors.toList());

        combinations = combinations.stream().filter(combination -> combination.getDifferentWith3000() < 0).collect(Collectors.toList());

        if (combinations.size() > 0) {
            Combination smallestCombination = combinations.get(0);

            smallestCombination = getCombination(combinations, smallestCombination);
            return smallestCombination;
        }
        return null;
    }

    private Combination getCombination(List<Combination> combinations, Combination smallestCombination) {
        for (int i = 1; i < combinations.size(); i++) {
            if (combinations.get(i).getDifferentWith3000() > smallestCombination.getDifferentWith3000()) {
                smallestCombination = combinations.get(i);
            }
        }
        return smallestCombination;
    }
}
