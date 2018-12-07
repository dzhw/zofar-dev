/**
 *
 */
package de.his.zofar.service.common.model;


/**
 * @author meisner
 *
 */
public class PageRequestDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -7305090092676354407L;
    private int pageNumber;
    private int pageSize;

    public PageRequestDTO() {
        super();
    }

    public PageRequestDTO(final int pageNumber, final int pageSize) {
        this();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

}
