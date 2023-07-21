package cinema.mapper;

public interface ResponseDtoMapper<D, T> {
    D mapToDto(T t);
}
