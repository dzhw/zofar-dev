package de.his.zofar.service.common.model;


// TODO reitmann remove factory, implement PageRequestDTO and PageRequest
public class BaseQueryDTO {

    private PageRequestDTO pageRequestDTO;

    // add order,sort here: learn from PageRequest

    public BaseQueryDTO() {
        super();
    }

    /**
     * @return the pageDTO
     */
    public PageRequestDTO getPageRequestDTO() {
        return pageRequestDTO;
    }

    /**
     * @param pageDTO the pageDTO to set
     */
    public void setPageRequestDTO(final PageRequestDTO pageDTO) {
        this.pageRequestDTO = pageDTO;
    }


}
