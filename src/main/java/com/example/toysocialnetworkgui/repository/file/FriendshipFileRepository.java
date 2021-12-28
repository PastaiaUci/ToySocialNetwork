package com.example.toysocialnetworkgui.repository.file;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.validators.Validator;
import com.example.toysocialnetworkgui.repository.repoExceptions.FileError;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.*;

public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Long,Long>, Friendship> {
    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes, int index_corrupted_file) {
        if(attributes.size() > FRIENDSHIP_NUMBER_OF_ATTRIBUTRES)
            throw new FileError(String.format("Corrupted file at line %d",index_corrupted_file));
        Friendship friendship = null;
        Long first_ID = null;
        Long second_ID = null;
        try{
            friendship = new Friendship(LocalDateTime.parse(attributes.get(2)));
            first_ID = Long.parseLong(attributes.get(0));
            second_ID = Long.parseLong(attributes.get(1));
        } catch(NumberFormatException | DateTimeParseException ex){
            throw  new FileError(String.format("Corrupted file at line %d",index_corrupted_file));
        }
        friendship.setId(new Tuple<>(first_ID,second_ID));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getDate();
    }
}
