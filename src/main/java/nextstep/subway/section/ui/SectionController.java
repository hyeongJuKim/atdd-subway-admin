package nextstep.subway.section.ui;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.dto.SectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines")
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService){
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        LineResponse line = sectionService.addSection(lineId, sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(line);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
