package spring.core.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.core.AppConfig;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinationNames = ac.getBeanDefinitionNames();
        for (String beanDefinationName : beanDefinationNames) {
            Object bean = ac.getBean(beanDefinationName);
            System.out.println("name = " + beanDefinationName + " object = " + bean);
        }
    }

    @Test
    @DisplayName("어플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinationName : beanDefinitionNames) {
            // Bean메타정보를 가지는 클래스 BeanDefinition
            // AnnotationConfigApplicationContext에 정의 되어있기 때문에 ac를 업케스팅해서 쓰면 사용하지 못한다.
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinationName);

            //Role ROLE_APPLICATIION : 직접 등록한 어플리케이션 빈
            //Role ROLE_iNFRASTRUCTURE : 스프링이 내무에서 사용한는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinationName);
                System.out.println("name = " + beanDefinationName + " object = " + bean);
            }
        }
    }

    public class nestedClass {
    }
}
