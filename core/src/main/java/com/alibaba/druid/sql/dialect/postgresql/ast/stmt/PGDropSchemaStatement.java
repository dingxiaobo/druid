/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class PGDropSchemaStatement extends SQLStatementImpl implements PGSQLStatement, SQLDropStatement {
    private SQLName schemaName;
    private List<SQLName> multipleNames = new ArrayList<>();
    private boolean ifExists;
    private boolean cascade;
    private boolean restrict;

    public SQLName getSchemaName() {
        return this.schemaName;
    }

    public void setSchemaName(SQLName schemaName) {
        this.schemaName = schemaName;
    }

    public List<SQLName> getMultipleNames() {
        return this.multipleNames;
    }

    public void setMultipleNames(List<SQLName> multipleNames) {
        this.multipleNames = multipleNames;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    public boolean isRestrict() {
        return restrict;
    }

    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            accept0((PGASTVisitor) visitor);
        }
    }

    @Override
    public void accept0(PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.schemaName);
        }
        visitor.endVisit(this);
    }
}
