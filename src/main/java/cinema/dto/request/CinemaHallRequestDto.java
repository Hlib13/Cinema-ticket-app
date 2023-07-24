package cinema.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CinemaHallRequestDto {
    @Min(10)
    private int capacity;
    @Size(max = 200)
    private String description;

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }
}
