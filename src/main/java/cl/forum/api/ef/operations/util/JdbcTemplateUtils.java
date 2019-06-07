package cl.forum.api.ef.operations.util;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateUtils {
    
    private SimpleJdbcCall simpleJdbcCall;
    private DataSource datasource;
    
    
    @Autowired
    public void setDatasource(DataSource dataSource) {
        this.datasource = dataSource;
        this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
    }
    
	public Map<String, Object> callStoreProcedure(String procedureName, Map<String, Object> parameters) {
        setDatasource(datasource);
        simpleJdbcCall.withProcedureName(procedureName);
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        if (null != parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        return simpleJdbcCall.execute(inParams);
    }
    

}