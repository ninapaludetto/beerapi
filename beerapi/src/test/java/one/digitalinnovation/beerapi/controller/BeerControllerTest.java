package one.digitalinnovation.beerapi.controller;

import one.digitalinnovation.beerapi.builder.BeerDTOBuilder;
import one.digitalinnovation.beerapi.dto.BeerDTO;
import one.digitalinnovation.beerapi.dto.QuantityDTO;
import one.digitalinnovation.beerapi.entity.Beer;
import one.digitalinnovation.beerapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerapi.exception.BeerStockExceededException;
import one.digitalinnovation.beerapi.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.JsonPath;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.beerapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BeerControllerTest {
    
    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2L;
    private static final String BEER_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String BEER_API_SUBPATH_DECREMENT_URL = "/decrement";
    
    private MockMvc mockMvc;
    
    @Mock
    private BeerService beerService;
    
    @InjectMocks
    private BeerController beerController;
    
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup((beerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s,locale) -> new MappingJackson2JsonView())
                .build();
    }
    
    @Test
    void whenPOSTIsCalledThenABeerisCreated() throws Exception{
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        
        when(beerService.createBeer(beerDTO)).thenReturn(beerDTO);
        
        mockMvc.perform((post(BEER_API_URL_PATH)
                .contentType((MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath(("$.type", is(beerDTO.getType().toString())))
    }
    
    @Test
    void whenPOSTIsCalledWithRequiredFieldThenIsReturned() throws Exception{
        
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setBrand((null);
        
        mockMvc.perform((post(BEER_API_URL_PATH)
                .contentType((MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception{
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        
        when(beerService.findByName(beerDTO.getName())).thenReturn(beerDTO);
        
        mockMvc.perform((MockMvcBuilders.get(BEER_API_URL_PATH + "/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOK())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("%.type", is(beerDTO.getType().toString())));
    }
    
    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception{
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        
        when(beerService.findByName(beerDTO.getName())).thenThrow(BeerNotFoundException.class);
        
        mockMvc.perform((MockMvcBuilders.get(BEER_API_URL_PATH + "/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void whenGETListWithBeerIsCalledThenOkStatusIsReturned() throws Exception{
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        
        when(BeerService.listAll()).thenReturn((Collections.singletonList((beerDTO));
        
        mockMvc.perform(MockMvcBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOK())
                .andExpect(jsonPath("$[0].name", is(BeerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(beerDTO.getType().toString())));
    }
    @Test
    void whenGETListWithoutBeerIsCalledThenOkStatusIsReturned() throws Exception{
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        mockMvc.perform((MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception{

        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        doNothing().when(beerService).deleteById((beerDTO.getId());

        mockMvc.perform((MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + beerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception{

        doThrow((BeerNotFoundException.class).when(beerService).deleteById(INVALID_BEER_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOkStatusisReturned() throws Exception{
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity((beerDTO.getQuantity() + quantityDTO.getQuantity());

        when(beerService.increment((VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        mockMvc.perform((MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(BeerDTO.getType().toString())))
                .andExpect(jsonPath(("$.quantity", is(beerDTO.getQuantity()))));
    }
}
