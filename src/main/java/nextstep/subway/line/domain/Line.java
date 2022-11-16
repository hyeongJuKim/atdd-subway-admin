package nextstep.subway.line.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.line.dto.LineRequest;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String color;
    @Embedded
    private Sections sections = Sections.create();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        sections.add(section);
        section.addLine(this);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Line of(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor());
    }

    public void updateInfo(Line updateLine) {
        this.name = updateLine.name;
        this.color = updateLine.color;
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
