package com.company.public_.parking.public_.parking_lots;

import com.company.public_.parking.public_.parking_lots.generated.GeneratedParkingLotsEntityStoreSerializerImpl;

import java.nio.ByteBuffer;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

/**
 * An {@link
 * com.speedment.enterprise.datastore.runtime.entitystore.EntityStoreSerializer}
 * class for table {@link com.speedment.runtime.config.Table} named
 * parking_lots.
 * <p>
 * This file is safe to edit. It will not be overwritten by the code generator.
 * 
 * @author company
 */
public final class ParkingLotsEntityStoreSerializerImpl extends GeneratedParkingLotsEntityStoreSerializerImpl {
    
    public ParkingLotsEntityStoreSerializerImpl(LongFunction<ByteBuffer> bufferFinder, LongToIntFunction offsetFinder) {
        super(bufferFinder, offsetFinder);
    }
}