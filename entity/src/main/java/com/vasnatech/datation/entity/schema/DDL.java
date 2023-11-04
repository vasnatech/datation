package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public class DDL extends Node {
    public DDL() {
        super("ddl");
    }

    public static class Schema extends DDL {
        final String schema;
        public Schema(String schema) {
            this.schema = schema;
        }

        public String getSchema() {
            return schema;
        }
        public static Builder builder() {
            return new Builder();
        }
        public static class Builder {
            String schema;
            public Builder schema(String schema) {
                this.schema = schema;
                return this;
            }
            public Schema build() {
                return new Schema(schema);
            }
        }
    }

    public static class Table extends DDL {
        final String table;
        public Table(String table) {
            this.table = table;
        }

        public String getTable() {
            return table;
        }
        public static Builder builder() {
            return new Builder();
        }
        public static class Builder {
            String table;
            public Builder table(String table) {
                this.table = table;
                return this;
            }
            public Table build() {
                return new Table(table);
            }
        }
    }

    public static abstract class Column extends DDL {
        public boolean isSimple() {
            return false;
        }
        public boolean isRelational() {
            return false;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            String column;
            RelationType type;
            RelationColumn.TableRelation.Builder tableBuilder;
            RelationColumn.TableRelation.Builder inverseTableBuilder;

            public Builder column(String column) {
                this.column = column;
                return this;
            }

            public Builder type(RelationType type) {
                this.type = type;
                return this;
            }

            RelationColumn.TableRelation.Builder tableBuilder() {
                if (tableBuilder == null) {
                    tableBuilder = RelationColumn.TableRelation.builder();
                }
                return tableBuilder;
            }

            public Builder table(String table) {
                tableBuilder().name(table);
                return this;
            }

            public Builder columns(LinkedHashMap<String, String> columns) {
                tableBuilder().columns(columns);
                return this;
            }

            public Builder column(String from, String to) {
                tableBuilder().column(from, to);
                return this;
            }

            RelationColumn.TableRelation.Builder inverseTableBuilder() {
                if (inverseTableBuilder == null) {
                    inverseTableBuilder = RelationColumn.TableRelation.builder();
                }
                return inverseTableBuilder;
            }

            public Builder inverseTable(String table) {
                inverseTableBuilder().name(table);
                return this;
            }

            public Builder inverseColumns(LinkedHashMap<String, String> columns) {
                inverseTableBuilder().columns(columns);
                return this;
            }

            public Builder inverseColumn(String from, String to) {
                inverseTableBuilder().column(from, to);
                return this;
            }
            public Column build() {
                return type == null
                        ? new SimpleColumn(column)
                        : new RelationColumn(
                                type,
                                tableBuilder == null ? null : tableBuilder.build(),
                                inverseTableBuilder == null ? null : inverseTableBuilder.build()
                        );
            }
        }
    }

    public static class SimpleColumn extends Column {
        final String column;
        public SimpleColumn(String column) {
            this.column = column;
        }

        public String getColumn() {
            return column;
        }

        public boolean isSimple() {
            return true;
        }
    }

    public static class RelationColumn extends Column {

        final RelationType type;
        final TableRelation table;
        final TableRelation inverseTable;

        public RelationColumn(RelationType type, TableRelation table, TableRelation inverseTable) {
            this.type = type;
            this.table = table;
            this.inverseTable = inverseTable;
        }

        public RelationType getType() {
            return type;
        }

        public TableRelation getTable() {
            return table;
        }

        public TableRelation getInverseTable() {
            return inverseTable;
        }

        public boolean isRelational() {
            return true;
        }

        public static class TableRelation {
            final String name;
            final LinkedHashMap<String, String> columns;

            public TableRelation(String name, LinkedHashMap<String, String> columns) {
                this.name = name;
                this.columns = columns;
            }

            public String getName() {
                return name;
            }

            public LinkedHashMap<String, String> getColumns() {
                return columns;
            }

            static TableRelation.Builder builder() {
                return new TableRelation.Builder();
            }

            static class Builder {
                String name;
                LinkedHashMap<String, String> columns = new LinkedHashMap<>(1);

                public TableRelation.Builder name(String name) {
                    this.name = name;
                    return this;
                }

                public TableRelation.Builder columns(LinkedHashMap<String, String> columns) {
                    this.columns = columns;
                    return this;
                }

                public TableRelation.Builder column(String from, String to) {
                    this.columns.put(from, to);
                    return this;
                }

                TableRelation build() {
                    return new TableRelation(name, columns);
                }
            }
        }
    }
}
