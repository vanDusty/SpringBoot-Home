package cn.van.parameter.validator.DTO;

import cn.van.parameter.validator.annoation.IdentityCardNumber;
import cn.van.parameter.validator.domain.assist.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @公众号： 风尘博客
 * @Classname UserDTO
 * @Description
 * @Date 2019/9/09 11:32 下午
 * @Author by Van
 */
@Data
public class UserDTO {

    /**
     * 用户id(只有在有Update分组中校验非空)
     */
    @NotNull(message = "id 不能为空", groups = Update.class)
    private Long userId;

    /**
     性别（不校验）
     */
    private String sex;

    /**
     用户名（校验：不能为空，不能超过20个字符串）
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字符")
    private String userName;

    /**
     * 手机号（校验：不能为空且按照正则校验格式）
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String mobile;

    /**
     邮箱（校验：不能唯恐且校验邮箱格式）
     */
    @NotBlank(message = "联系邮箱不能为空")
    @Email(message = "邮箱格式不对")
    private String email;

    /**
     * 身份证号（校验：自定义注解校验）
     */
    @IdentityCardNumber
    private String idNumber;

    private String passWord;

    //    @NotNull/@NotEmpty/@NotBlank 区别
    //    1.@NotNull：不能为null，但可以为empty
    //    2.@NotEmpty：不能为null，而且长度必须大于0
    //    3.@NotBlank：只能作用在String上，不能为null，而且调用trim()后，长度必须大于0，即：必须有实际字符
}
