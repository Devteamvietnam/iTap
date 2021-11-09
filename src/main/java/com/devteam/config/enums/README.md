# Enum trong Java


## Nội Dung

1. Enum là gì?
2. Khai báo Enum.
3. Truy xuất giá trị trong enum.
4. Constructor trong enum.
5. Các ví dụ.


# 1. Enum là gì?


Enum trong Java là một kiểu dữ liệu đặc biệt, là một tập hợp các hằng số được xác định trước.

Enum có thể khai báo bên ngoài class, bên trong class hoặc cũng có thể nằm ở một file riêng biệt.

Enum có thể chứa các thành phần như một object bình thường. Có thể khai báo thuộc tính, phương thức, constructor.

Tất cả các enum đều được extend từ java.lang.Enum, nên nó cũng có sẵn một số các phương thức bên trong enum.


# 2. Khai báo Enum.

Để khai báo enum trong Java các bạn sử dụng cú pháp sau:

```bash
access_modify enum name {
	// thanh phan ben trong
}
```

Trong đó:

***access_modify*** là phạm vi truy cập của enum (**public**, **protected**, **private**, **default**), hoặc cũng có thể để trống.
name là tên của enum.

VD: Khai báo một enum chứa các ngày trong tuần.

```bash
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY 
}
```

VD: Khai báo một enum có method bên trong.

```bash
public enum Day {
  SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
  THURSDAY, FRIDAY, SATURDAY;

  public static void printAll() {
    for (Day day : Day.values()) {
      System.out.println(day);
    }
  }
}
```

# 3. Truy xuất giá trị trong enum.

```bash
enumName.property
```
Trong đó:
- enumName là tên của enum chúng ta cần truy xuất.
- property là hằng số các bạn cần truy xuất.

Bạn cũng có thể dùng vòng lặp for để duyệt qua các phần tử của enum bằng các sử dụng phương thức values().

```bash
public class Main {
  public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY
  }

  public static void main(String[] args) {
    for (Day day : Day.values()) {
      System.out.println(day);
    }
  }
}
```
# 4. Constructor trong enum.

Trong enum bạn cũng có thể khai báo constructor và khởi tạo với các giá trị đặc biệt kèm theo.

Khi một enum đã khai báo contructor thì các hằng số bên trong nó cũng cần phần khai báo thêm các tham số truyền vào constructor như đã định nghĩa

VD: Khai báo enum HoliDay và định nghĩa constructor truyền vào day, month.

```bash
enum HoliDay {
  WESTERN_NEW_YEAR(1, 1),
  CHRISTMAS(25, 12);

  private final int day;

  private final int month;

  public int getDay() {
    return day;
  }

  public int getMonth() {
    return month;
  }

  HoliDay(int day, int month) {
    this.day = day;
    this.month = month;
  }
}

public class Main {

  public static void main(String[] args) {
    System.out.println(HoliDay.CHRISTMAS.getDay());
    System.out.println(HoliDay.CHRISTMAS.getMonth());
  }
}
```
Ở ví dụ trên như các bạn đã thấy thì do enum HoliDay có khai báo constructor và truyền vào 2 param nên ở các hằng số WESTERN_NEW_YEAR và CHRISTMAS cũng cần phải truyền vào 2 param thì mới có thể hoạt động được.

# 5. Các ví dụ.
VD: Sử dụng enum trong câu lệnh switch case.

```bash
public class EnumTest {
    Day day;
    
    public EnumTest(Day day) {
        this.day = day;
    }
    
    public void tellItLikeItIs() {
        switch (day) {
            case MONDAY:
                System.out.println("Mondays are bad.");
                break;
                    
            case FRIDAY:
                System.out.println("Fridays are better.");
                break;
                         
            case SATURDAY: case SUNDAY:
                System.out.println("Weekends are best.");
                break;
                        
            default:
                System.out.println("Midweek days are so-so.");
                break;
        }
    }
    
    public static void main(String[] args) {
        EnumTest firstDay = new EnumTest(Day.MONDAY);
        firstDay.tellItLikeItIs();
        EnumTest thirdDay = new EnumTest(Day.WEDNESDAY);
        thirdDay.tellItLikeItIs();
        EnumTest fifthDay = new EnumTest(Day.FRIDAY);
        fifthDay.tellItLikeItIs();
        EnumTest sixthDay = new EnumTest(Day.SATURDAY);
        sixthDay.tellItLikeItIs();
        EnumTest seventhDay = new EnumTest(Day.SUNDAY);
        seventhDay.tellItLikeItIs();
    }
}
```
Kết quả:

```bash
Mondays are bad.
Midweek days are so-so.
Fridays are better.
Weekends are best.
Weekends are best.
```
VD: So sánh giá trị trong enum.

```bash
public class Main {
    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY
    }

    public static void main(String[] args) {
        Day monDay = Day.MONDAY;
        // so sán bằng toán tử so sánh
        if (monDay == Day.MONDAY) {
            System.out.println("Equal");
        }

        // so sánh bằng phương thức.
        if (monDay.equals(Day.MONDAY)) {
            System.out.println("Equal");
        }
    }
}
```
## License
[MIT](https://choosealicense.com/licenses/mit/)