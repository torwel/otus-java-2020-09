package ru.otus.torwel.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityCMD;
    private String cacheSelectAllSql;
    private String cacheSelectByIdSql;
    private String cacheInsertSql;
    private String cacheUpdateSql;


    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityCMD) {
        this.entityCMD = entityCMD;
    }

    // SELECT * FROM tableName
    @Override
    public String getSelectAllSql() {
        if (cacheSelectAllSql == null) {
            cacheSelectAllSql = "SELECT * FROM " + entityCMD.getName().toLowerCase();
        }
        return cacheSelectAllSql;
    }

    // SELECT * FROM tableName WHERE idName = ?
    // If class have not @Id annotated field, returns getSelectAllSql().
    @Override
    public String getSelectByIdSql() {
        if (cacheSelectByIdSql != null) {
            return cacheSelectByIdSql;
        }
        Field idField = entityCMD.getIdField();
        if (idField == null) {
            cacheSelectByIdSql = getSelectAllSql();
        }
        else {
            String request = String.format("SELECT * FROM %s WHERE %s = ?",
                    entityCMD.getName().toLowerCase(),
                    idField.getName().toLowerCase()
                    );
            cacheSelectByIdSql = request.toString();
        }
        return cacheSelectByIdSql;
    }

    // INSERT INTO tableName (field1,field2) VALUES (?,?)
    @Override
    public String getInsertSql() {
        if (cacheInsertSql != null) {
            return cacheInsertSql;
        }
        List<Field> fields = entityCMD.getFieldsWithoutId();
        StringBuilder request = new StringBuilder();
        StringBuilder values = new StringBuilder();

        request.append("INSERT INTO ").append(entityCMD.getName().toLowerCase()).append(" (");
        for (Field field : fields) {
            request.append(field.getName()).append(",");
            values.append("?,");
        }
        values.deleteCharAt(values.length() - 1);
        values.append(')');
        request.deleteCharAt(request.length() - 1);
        request.append(") VALUES (").append(values);
        return cacheInsertSql = request.toString();
    }

    // UPDATE tableName SET field1 = ?, field2 = ?, ... fieldN = ? WHERE idName = ?
    // If class have not @Id annotated field, returns "".
    @Override
    public String getUpdateSql() {
        if (cacheUpdateSql != null) {
            return cacheUpdateSql;
        }
        Field idField = entityCMD.getIdField();
        if (idField == null) {
            cacheUpdateSql = "";
        }
        else {
            List<Field> fields = entityCMD.getFieldsWithoutId();
            StringBuilder request = new StringBuilder();

            request.append("UPDATE ").append(entityCMD.getName().toLowerCase()).append(" SET ");
            for (Field field : fields) {
                request.append(field.getName()).append(" = ?,");
            }
            request.deleteCharAt(request.length() - 1)
                    .append(" WHERE ")
                    .append(idField.getName().toLowerCase())
                    .append(" = ?");
            cacheUpdateSql = request.toString();
        }
        return cacheUpdateSql;
    }
}
