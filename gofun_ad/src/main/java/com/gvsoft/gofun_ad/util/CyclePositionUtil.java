package com.gvsoft.gofun_ad.util;

public class CyclePositionUtil {

    public static int getRealPosition(int position, int realItemCount) {
        int fixPosition;
        if (position == 0) {
            fixPosition = realItemCount - 1;
        } else if (position == realItemCount + 1) {
            fixPosition = 0;
        } else {
            fixPosition = position - 1;
        }
        return fixPosition;
    }
}
