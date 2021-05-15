package pw.cdmi.aws.edu.book.modules;

/**
 * 书页的大小
 */
public enum PageSize {
    A0(2384, 3370),
    A1(1684, 2384),
    A2(1190, 1684),
    A3(842, 1190),
    A4(595, 842),
    A5(420, 595),
    A6(298, 420),
    A7(210, 298),
    A8(148, 210),
    A9(105, 547),
    A10(74, 105),

    B0(2834, 4008),
    B1(2004, 2834),
    B2(1417, 2004),
    B3(1000, 1417),
    B4(708, 1000),
    B5(498, 708),
    B6(354, 498),
    B7(249, 354),
    B8(175, 249),
    B9(124, 175),
    B10(88, 124),
    ;
    private int width;
    private int height;
    PageSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }
}
