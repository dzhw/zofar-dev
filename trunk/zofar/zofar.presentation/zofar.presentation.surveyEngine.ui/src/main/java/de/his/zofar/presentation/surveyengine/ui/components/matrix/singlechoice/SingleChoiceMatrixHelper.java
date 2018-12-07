/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice;


/**
 * @author le
 *
 */
@Deprecated
class SingleChoiceMatrixHelper {

    private SingleChoiceMatrixHelper() {
        super();
    }

    static final SingleChoiceMatrixHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

//    void setChildrenDifferential(final List<UIComponent> children,
//            final Boolean isDifferential) {
//        for (final UIComponent child : children) {
//            if (UISingleChoiceMatrixItem.class.isAssignableFrom(child
//                    .getClass())) {
////                ((UISingleChoiceMatrixItem) child)
////                        .setDifferential(isDifferential);
//            } else if (UISingleChoiceMatrixResponseDomainUnit.class
//                    .isAssignableFrom(child.getClass())) {
////                ((UISingleChoiceMatrixResponseDomainUnit) child)
////                        .setDifferential(isDifferential);
//            } else if (UISort.class.isAssignableFrom(child.getClass())) {
//                this.setChildrenDifferential(child.getChildren(),
//                        isDifferential);
//            }
//        }
//    }

    /**
     * SingletonHolder is loaded on the first execution of
     * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
     * not before.
     */
    private static class SingletonHolder {
        private static final SingleChoiceMatrixHelper INSTANCE = new SingleChoiceMatrixHelper();
    }

}
