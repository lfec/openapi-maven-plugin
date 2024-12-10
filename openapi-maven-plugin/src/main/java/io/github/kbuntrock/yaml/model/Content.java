package io.github.kbuntrock.yaml.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.kbuntrock.model.DataObject;
import io.github.kbuntrock.model.ParameterObject;
import io.github.kbuntrock.utils.OpenApiDataType;
import io.github.kbuntrock.utils.OpenApiTypeResolver;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public class Content {

	@JsonIgnore
	private Schema schema;

	@JsonIgnore
	protected Object example;
	@JsonIgnore
	protected Object examples;

	public static Content fromDataObject(final ParameterObject parameterObject) {

		final Content content = fromDataObject((DataObject) parameterObject);

		final boolean isMultipartFile = MultipartFile.class == parameterObject.getJavaClass() ||
			(OpenApiDataType.ARRAY == parameterObject.getOpenApiResolvedType().getType()
				&& MultipartFile.class == parameterObject.getArrayItemDataObject().getJavaClass());

		if(isMultipartFile) {
			// the MultipartFile must be named in the body.
			final Content multipartContent = new Content();
			final Schema schema = new Schema();
			multipartContent.schema = schema;
			if(parameterObject.isRequired()) {
				schema.setRequired(Collections.singletonList(parameterObject.getName()));
			}
			schema.setType(OpenApiTypeResolver.OBJECT_TYPE);
			final Map<String, Property> propertyMap = new LinkedHashMap<>();
			schema.setProperties(propertyMap);
			final Property property = new Property(content.getSchema());
			propertyMap.put(parameterObject.getName(), property);
			return multipartContent;
		}
		return content;
	}

	public static Content fromDataObject(final DataObject dataObject) {
		if(dataObject == null) {
			return null;
		}
		final Set<String> exploredSignatures = new HashSet<>();
		final Content content = new Content();
		content.schema = new Schema(dataObject, exploredSignatures);
		return content;
	}

	public Schema getSchema() {
		return schema;
	}

	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}

	public Object getExamples() {
		return examples;
	}

	public void setExamples(Object examples) {
		this.examples = examples;
	}

	@JsonAnyGetter
	public Map<String, Object> getJsonObject() {
		final Map<String, Object> map = new LinkedHashMap<>();
		if(schema != null) {
			map.put("schema", schema);
		}
		if(example != null ) {
			map.put("example", example);
		}
		if(examples != null ) {
			map.put("examples", examples);
		}

		return map;
	}

}
