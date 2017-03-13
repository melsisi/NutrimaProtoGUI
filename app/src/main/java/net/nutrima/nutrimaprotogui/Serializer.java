package net.nutrima.nutrimaprotogui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class Serializer {

    public static ByteBuffer serialize(LambdaRespAll all) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(all);
            }
            ByteBuffer buffer = ByteBuffer.allocate(b.size());
            buffer.put(b.toByteArray(), 0, b.size());
            buffer.position(0);
            return buffer;
        }
    }

    public static LambdaRespAll deserialize(ByteBuffer bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes.array())){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return ((LambdaRespAll) o.readObject());
            }
        }
    }

}