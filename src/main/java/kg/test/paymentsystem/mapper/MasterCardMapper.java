package kg.test.paymentsystem.mapper;

import kg.test.paymentsystem.dto.CardRequestDto;
import kg.test.paymentsystem.entity.card.MasterCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MasterCardMapper {

    @Mapping(target = "bankName",source = "bankName")
    @Mapping(target = "type",source = "type")
    @Mapping(target = "cardNumber",source = "cardNumber")
    @Mapping(target = "issueDate",source = "issueDate")
    CardRequestDto mapToDto(MasterCard masterCard);
}
