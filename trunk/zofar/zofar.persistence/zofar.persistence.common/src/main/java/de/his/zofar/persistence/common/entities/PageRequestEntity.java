/**
 * 
 */
package de.his.zofar.persistence.common.entities;

import org.springframework.data.domain.Sort;

/**
 * @author meisner
 * 
 */
public class PageRequestEntity {

    private int pageNumber;
    private int pageSize;

    // private Sort sort;

    public PageRequestEntity() {
        super();
    }

    public int getOffset() {

        return this.pageNumber * this.pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public Sort getSort() {
        throw new RuntimeException("Not yet implemented!");
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(final Sort sort) {
        throw new RuntimeException("Not yet implemented!");
    }

}
