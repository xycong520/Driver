package cn.jdywl.driver.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines constants for accessing the content provider defined in DBProvider. A content provider
 * contract assists in accessing the provider's available content URIs, column names, MIME types,
 * and so forth, without having to know the actual values.
 */
public final class DBProviderContract implements BaseColumns {

    private DBProviderContract() {
    }

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "cn.jdywl.driver";

    /**
     * The DBProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    /**
     * The MIME type for a content URI that would return multiple rows
     * <P>Type: TEXT</P>
     */
    public static final String MIME_TYPE_ROWS =
            "vnd.android.cursor.dir/vnd.com.example.android.threadsample";

    /**
     * The MIME type for a content URI that would return a single row
     * <P>Type: TEXT</P>
     */
    public static final String MIME_TYPE_SINGLE_ROW =
            "vnd.android.cursor.item/vnd.com.example.android.threadsample";

    /**
     * primary key column name
     */
    public static final String ROW_ID = BaseColumns._ID;

    /**
     * 创建时间
     */
    public static final String CREATED_AT = "created_at";

    /**
     * 更新时间
     */
    public static final String UPDATED_AT = "updated_at";

    /**
     * Card id,在多个表中使用
     */
    public static final String CARD_ID_COLUMN = "card_id";

    /**
     * Contact table name
     */
    public static final String CONTACT_TABLE_NAME = "contact";

    /**
     * Contact table content URI
     */
    public static final Uri CONTACT_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, CONTACT_TABLE_NAME);

    /**
     * Contact table contact name column name
     */
    public static final String NAME_COLUMN = "name";

    /**
     * Session table name
     */
    public static final String SESSION_TABLE_NAME = "session";

    /**
     * Session table content URI
     */
    public static final Uri SESSION_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, SESSION_TABLE_NAME);


    /**
     * Session table message number column name
     */
    public static final String MESSAGE_COUNT_COLUMN = "message_count";

    /**
     * Session table snippet column name
     */
    public static final String SNIPPET_COLUMN = "snippet";

    /**
     * Session table date column name
     */
    public static final String DATE_COLUMN = "date";

    /**
     * Message table name
     */
    public static final String MESSAGE_TABLE_NAME = "message";

    /**
     * Content URI for message table
     */
    public static final Uri MESSAGE_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, MESSAGE_TABLE_NAME);


    /**
     * Message table message column name
     */
    public static final String CONTENT_COLUMN = "content";

    /**
     * Message table if read column name
     */
    public static final String IFREAD_COLUMN = "ifread";

    /**
     * Message table if send column name
     */
    public static final String IFSEND_COLUMN = "ifsend";

    /**
     * Message table time column name
     */
    public static final String TIME_COLUMN = "time";

    /**
     * 运输线路和价格数据表
     */
    public static final String PRICE_TABLE_NAME = "price";

    /**
     * Price table content URI
     */
    public static final Uri PRICE_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, PRICE_TABLE_NAME);

    /**
     * price表的ID
     */
    public static final String PRICE_ID_COLUMN = "price_id";

    /**
     * 始发地
     */
    public static final String PRICE_ORIGIN_COLUMN = "origin";

    /**
     * 目的地
     */
    public static final String PRICE_DESTINATION_COLUMN = "destination";

    /**
     * 小轿车运费
     */
    public static final String PRICE_CAR_COLUMN = "car_price";
    /**
     * SUV运费
     */
    public static final String PRICE_SUV_COLUMN = "suv_price";
    /**
     * 特种车辆运费
     */
    public static final String PRICE_BIGSUV_COLUMN = "bigsuv_price";

    /**
     * 提车费
     */
    public static final String PRICE_PLATFORMFEE_COLUMN = "platformfee";

    /**
     * 备注
     */
    public static final String PRICE_DESC_COLUMN = "desc";


    // The content provider database name
    public static final String DATABASE_NAME = "jdywl_db";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;
}
