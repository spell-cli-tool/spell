package org.spell.spring.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.spell.spring.Action;
import org.spell.spring.client.model.BootVersionElement;
import org.spell.spring.client.model.DependenciesElement;
import org.spell.spring.client.model.JavaVersionElement;
import org.spell.spring.client.model.LanguageElement;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.PackagingElement;
import org.spell.spring.client.model.TypeElement;

class InitializerClientTest {

  private static MetadataDto metadata;

  private static Set<String> actions = Set.of(Action.STARTER_ZIP.getValue(),
      Action.BUILD_GRADLE.getValue(), Action.POM_XML.getValue());

  @BeforeAll
  public static void init() {
    InitializerRestProperties properties = new InitializerRestProperties();
    InitializerClient client = new InitializerClient(properties);
    metadata = client.retrieveMetadata();
  }

  @Test
  void testTypeElement() {
    TypeElement element = metadata.getType();
    assertEquals(5, element.getValues().size(), "Expected 5 project types");
    element.getValues().forEach(v -> assertTrue(actions.contains(v.getAction()),
        String.format("For type value %s found unexpected action %s", v.getId(), v.getAction())));
  }

  @Test
  void testLanguageElement() {
    LanguageElement element = metadata.getLanguage();
    assertEquals(3, element.getValues().size(), "Expected 3 languages");
  }

  @Test
  void testJavaVersionElement() {
    JavaVersionElement element = metadata.getJavaVersion();
    assertEquals(2, element.getValues().size(), "Expected 2 java versions");
  }

  @Test
  void testBootVersionElement() {
    BootVersionElement element = metadata.getBootVersion();
    assertEquals(6, element.getValues().size(), "Expected 6 Spring Boot versions");
  }

  @Test
  void testPackagingElement() {
    PackagingElement element = metadata.getPackaging();
    assertEquals(2, element.getValues().size(), "Expected 2 packaging elements");
  }

  @Test
  void testDependencies() {
    DependenciesElement element = metadata.getDependencies();
    assertEquals(20, element.getValues().size(), "Expected 20 dependencies");
  }
}