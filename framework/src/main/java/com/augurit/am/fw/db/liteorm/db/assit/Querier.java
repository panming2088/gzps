package com.augurit.am.fw.db.liteorm.db.assit;


import com.augurit.am.fw.db.liteorm.log.OrmLog;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * 辅助查询
 *
 * @author MaTianyu
 * @date 2013-6-15下午11:11:02
 */
public final class Querier {
    private static final String TAG = Querier.class.getSimpleName();

    private Querier() {
    }

    /**
     * 因为每个查询都不一样，但又有相同的结构，这种形式维持代码的统一性，也可以个性化解析。
     */
    public static <T> T doQuery(SQLiteDatabase db, SQLStatement st, CursorParser<T> parser) {
        if (OrmLog.isPrint) {
            OrmLog.d(TAG, "----> Query Start: " + st.toString());
        }
        Cursor cursor = db.rawQuery(st.sql, (String[]) st.bindArgs);
        if (cursor != null) {
            parser.process(db, cursor);
            if (OrmLog.isPrint) {
                OrmLog.d(TAG, "<---- Query End , cursor size : " + cursor.getCount());
            }
        } else {
            if (OrmLog.isPrint) {
                OrmLog.e(TAG, "<---- Query End : cursor is null");
            }
        }
        return parser.returnResult();
    }

    /**
     * A simple cursor parser
     *
     * @author MaTianyu
     */
    public static abstract class CursorParser<T> {
        private boolean parse = true;

        public final void process(SQLiteDatabase db, Cursor cursor) {
            try {
                cursor.moveToFirst();
                while (parse && !cursor.isAfterLast()) {
                    parseEachCursor(db, cursor);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        public final void stopParse() {
            parse = false;
        }

        public T returnResult() {
            return null;
        }

        public abstract void parseEachCursor(SQLiteDatabase db, Cursor c) throws Exception;
    }
}
