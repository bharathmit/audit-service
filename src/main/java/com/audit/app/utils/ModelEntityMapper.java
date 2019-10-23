package com.audit.app.utils;




import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import net.bytebuddy.description.method.MethodDescription.TypeToken;



public class ModelEntityMapper {
	
	private final static Logger log = LoggerFactory.getLogger(ModelEntityMapper.class.getClass());
	
	public static Object converObjectToPoJo(Object modelObject,Class entityName){
		try{
			
			if(modelObject==null)return null;
			
			ModelMapper modelMapper = new ModelMapper();
			 modelMapper.getConfiguration().setFieldMatchingEnabled(true);
			return modelMapper.map(modelObject, entityName);
			
		}
		catch(Exception e){
			log.error("Error while Conver ModelToEntity "+entityName.getName(), e);
			return null;
		}
	
	}
	
	
	/*public static <T> List<?> convertListToCollection(List<T> list){
		try{
			if(CollectionUtils.isEmpty(list))
				return null;
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setFieldMatchingEnabled(true);
			return modelMapper.map(list, new TypeToken<List<?>>() {}.getType());
		}
		catch(Exception e){
			log.error("Error while Conver ListToCollection ", e);
			return null;
		}
		
	}*/

	
	
	
	
	
	

}
