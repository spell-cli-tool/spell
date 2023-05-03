package org.spell;

import org.spell.spring.client.model.BootVersionElement;
import org.spell.spring.client.model.BootVersionValue;
import org.spell.spring.client.model.DependenciesElement;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.Guide;
import org.spell.spring.client.model.JavaVersionElement;
import org.spell.spring.client.model.JavaVersionValue;
import org.spell.spring.client.model.LanguageElement;
import org.spell.spring.client.model.LanguageValue;
import org.spell.spring.client.model.Links;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.MetadataElement;
import org.spell.spring.client.model.MetadataValue;
import org.spell.spring.client.model.PackagingElement;
import org.spell.spring.client.model.PackagingValue;
import org.spell.spring.client.model.Reference;
import org.spell.spring.client.model.TextElement;
import org.spell.spring.client.model.TypeElement;
import org.spell.spring.client.model.TypeValue;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RegisterReflectionForBinding({
		BootVersionElement.class,
		BootVersionValue.class,
		DependenciesElement.class,
		DependenciesGroup.class,
		DependenciesValue.class,
		Guide.class,
		JavaVersionElement.class,
		JavaVersionValue.class,
		LanguageElement.class,
		LanguageValue.class,
		Links.class,
		MetadataDto.class,
		MetadataElement.class,
		MetadataValue.class,
		PackagingElement.class,
		PackagingValue.class,
		Reference.class,
		TextElement.class,
		TypeElement.class,
		TypeValue.class})
@SpringBootApplication
public class SpellApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpellApplication.class, args);
	}

}
