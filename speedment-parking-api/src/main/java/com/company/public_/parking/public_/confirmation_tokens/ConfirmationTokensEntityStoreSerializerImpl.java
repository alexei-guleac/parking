package com.company.public_.parking.public_.confirmation_tokens;

import com.company.public_.parking.public_.confirmation_tokens.generated.GeneratedConfirmationTokensEntityStoreSerializerImpl;

import java.nio.ByteBuffer;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

/**
 * An {@link
 * com.speedment.enterprise.datastore.runtime.entitystore.EntityStoreSerializer}
 * class for table {@link com.speedment.runtime.config.Table} named
 * confirmation_tokens.
 * <p>
 * This file is safe to edit. It will not be overwritten by the code generator.
 * 
 * @author company
 */
public final class ConfirmationTokensEntityStoreSerializerImpl extends GeneratedConfirmationTokensEntityStoreSerializerImpl {
    
    public ConfirmationTokensEntityStoreSerializerImpl(LongFunction<ByteBuffer> bufferFinder, LongToIntFunction offsetFinder) {
        super(bufferFinder, offsetFinder);
    }
}