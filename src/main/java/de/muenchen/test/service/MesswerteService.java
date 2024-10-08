package de.muenchen.test.service;

import de.muenchen.test.domain.FzTyp;
import de.muenchen.test.domain.Mapper;
import de.muenchen.test.domain.MesswerteFormatBuilder;
import de.muenchen.test.domain.MqMesswerte;
import de.muenchen.test.domain.MqMesswerteDTO;
import de.muenchen.test.repository.MqMesswerteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MesswerteService {

    private final MqMesswerteRepository repo;
    private final Mapper mapper = new Mapper();

    public MqMesswerteDTO loadMesswerteByYear(Integer year) {
        List<MqMesswerte> messwerte = repo.findByDatumVon(LocalDateTime.of(year, 1, 1, 0, 0, 0));
        MqMesswerteDTO messwerteDTO = mapper.map(messwerte);
        return messwerteDTO;
    }

    public MqMesswerteDTO loadMesswerte(List<String> messquerschnitte, LocalDateTime datumVon, LocalDateTime datumBis, Optional<LocalTime> uhrzeitVon, Optional<LocalTime> uhrzeitBis, List<Integer> tagestypen, Optional<List<FzTyp>> fzTypen, Optional<Integer> limit, Optional<Integer> page) {
        List<MqMesswerte> messwerte = null;
            if (uhrzeitVon.isEmpty() || uhrzeitBis.isEmpty()) {
                messwerte = repo.findByDatumAndTagestypenAndFzTypen(messquerschnitte.get(0), datumVon, datumBis);
            } else {
                //                messwerte = repo.findByDatumAndUhrzeitAndTagestypenAndFzTypen(); TODO
            }
            //            if (page > resultPage.getTotalPages()) { TODO
            //                throw new MyResourceNotFoundException();
            //            }
        List<FzTyp> fzTypenList;
        fzTypenList = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));

        MqMesswerteDTO messwerteDTO = mapper.map(messwerte, fzTypenList);
        return messwerteDTO;
    }
}
