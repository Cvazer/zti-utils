package by.zti.main.serializer;

import java.io.*;

public class Serializer<T> {
    private File file;

    public Serializer() {
        this("NULL");
    }

    public Serializer(String path) {
        file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Serializer(File file) {
        this.file = file;
    }

    public void serialize(T obj){
        try {
            FileOutputStream fis = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fis);
            out.writeObject(obj);
            out.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T deserialize(){
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
