package one.digitalinnovation.beerapi.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import one.digitalinnovation.beerapi.dto.BeerDTO;
import one.digitalinnovation.beerapi.enums.BeerType;

@Builder
public class BeerDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Eisenbahn";

    @Builder.Default
    private String brand = "Brasil Kirin";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private BeerType type = BeerType.LAGER;

    public BeerDTO toBeerDTO(){
        return new BeerDTO(id
        name,
        brand,
        max,
        quantity,
        type);
    }
}
