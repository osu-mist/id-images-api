package edu.oregonstate.mist.idimages

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import java.sql.Blob
import edu.oregonstate.mist.contrib.AbstractIdImageDao

@RegisterMapper(ImageMapper)
public interface IDImageDAO extends Closeable {
    @SqlQuery(AbstractIdImageDao.checkID)
    Integer getPIDM(@Bind("id") String id)

    @SqlQuery(AbstractIdImageDao.getByID)
    Blob getByID(@Bind("id") String id)

    @Override
    void close()
}
