package de.his.zofar.service.common.model;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T extends BaseDTO> {

    private List<T> content;

    private int totalPages;

    private int totalElements;

    private boolean hasPreviousPage;

    private PageRequestDTO pageRequest;

    private boolean isFirstPage;

    private boolean isLastPage;

    private boolean hasNextPage;

    /**
     * Default constructor required by Dozer.
     */
    public PageDTO() {
        this.content = new ArrayList<T>();
    }

    public void add(final T dto) {
        this.content.add(dto);
    }

    /**
     * Returns the page content as {@link List}.
     * 
     * @return
     */
    public List<T> getContent() {
        return this.content;
    }

    /**
     * Returns the number of the current page. Is always positive and less that
     * {@code Page#getTotalPages()}.
     * 
     * @return the number of the current page
     */
    public int getNumber() {
        return this.pageRequest.getPageNumber();
    }

    /**
     * Returns the number of elements currently on this page.
     * 
     * @return the number of elements currently on this page
     */
    public int getNumberOfElements() {
        return this.content.size();
    }

    /**
     * Returns the size of the page.
     * 
     * @return the size of the page
     */
    public int getSize() {
        return this.content.size();
    }

    // /**
    // * Returns the sorting parameters for the page.
    // *
    // * @return
    // */
    // public Sort getSort() {
    // return this.pageRequest.getSort();
    // }

    /**
     * Returns the total amount of elements.
     * 
     * @return the total amount of elements
     */
    public long getTotalElements() {
        return this.totalElements;
    }

    /**
     * Returns the number of total pages.
     * 
     * @return the number of toral pages
     */
    public int getTotalPages() {
        return this.totalPages;
    }

    /**
     * Returns whether the {@link Page} has content at all.
     * 
     * @return
     */
    public boolean hasContent() {
        return this.content.isEmpty();
    }

    /**
     * Returns if there is a next page.
     * 
     * @return if there is a next page
     */
    public boolean hasNextPage() {
        return this.hasNextPage;
    }

    /**
     * Returns if there is a previous page.
     * 
     * @return if there is a previous page
     */
    public boolean hasPreviousPage() {
        return this.hasPreviousPage;
    }

    /**
     * Returns whether the current page is the first one.
     * 
     * @return
     */
    public boolean isFirstPage() {
        return this.isFirstPage;
    }

    /**
     * Returns whether the current page is the last one.
     * 
     * @return
     */
    public boolean isLastPage() {
        return this.isLastPage;
    }

    public void setContent(final List<T> content) {
        this.content = content;
    }

    public void setFirstPage(final boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public void setHasNextPage(final boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setHasPreviousPage(final boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public void setLastPage(final boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public void setPageRequest(final PageRequestDTO pageRequest) {
        this.pageRequest = pageRequest;
    }

    public void setTotalElements(final int totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(final int totalPages) {
        this.totalPages = totalPages;
    }

}
