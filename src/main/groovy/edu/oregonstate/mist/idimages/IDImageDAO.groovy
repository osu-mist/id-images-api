package edu.oregonstate.mist.idimages

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import java.sql.Blob

@RegisterMapper(ImageMapper)
public interface IDImageDAO extends Closeable {
    @SqlQuery("""
       redacted
        """)
    Blob getByID(@Bind("id") Integer id)

    @Override
    void close()
}
