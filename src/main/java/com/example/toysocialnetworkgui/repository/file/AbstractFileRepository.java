package com.example.toysocialnetworkgui.repository.file;

import com.example.toysocialnetworkgui.domain.Entity;
import com.example.toysocialnetworkgui.domain.validators.ValidationException;
import com.example.toysocialnetworkgui.domain.validators.Validator;
import com.example.toysocialnetworkgui.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factory (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
       super(validator);
       this.fileName = fileName;
       loadData();
    }

    private void loadData() {
       try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
           String linie;
           int possible_corrupted_line_index = 0;
           while( (linie = bufferedReader.readLine()) != null ) {
               possible_corrupted_line_index ++;
               List<String> attributes = Arrays.asList(linie.split(";"));
               E entity = extractEntity(attributes,possible_corrupted_line_index);
               super.save(entity);
           }
       }
       catch (IOException ex){
           System.out.println("fsafsafasf");
           ex.printStackTrace();
       }
       catch (ValidationException ex) {
           System.out.println("fsafsafasf");
            ex.printStackTrace();
       }
    }

    protected void appendToFile(E entity){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))){
            bufferedWriter.flush();
            bufferedWriter.write(createEntityAsString(entity));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    protected void writeAllToFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false))){
            for(Map.Entry<ID,E> entry : entities.entrySet()){
                bufferedWriter.write(createEntityAsString(entry.getValue()));
                bufferedWriter.newLine();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     *          list of attributes extracted from the file
     * @param index_corrupted_line
     *          number of the possibly corrupted line in the file
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes, int index_corrupted_line);

    /**
     * converts the entity of type E and its attributes into a string to store in the file
     * @param entity
     *          entity to be converted
     * @return
     *          the string created
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public void save(E entity){
        super.save(entity);
        appendToFile(entity);
    }

    @Override
    public void delete(ID id){
        super.delete(id);
        writeAllToFile();
    }

    @Override
    public void update(E entity){
        super.update(entity);
        writeAllToFile();
    }
}

