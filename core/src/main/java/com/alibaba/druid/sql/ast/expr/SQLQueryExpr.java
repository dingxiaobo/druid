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
package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SQLQueryExpr extends SQLExprImpl implements Serializable {
    private static final long serialVersionUID = 1L;
    public SQLSelect subQuery;

    public SQLQueryExpr() {
    }

    public SQLQueryExpr(SQLSelect select) {
        setSubQuery(select);
    }

    public SQLSelect getSubQuery() {
        return this.subQuery;
    }

    public void setSubQuery(SQLSelect subQuery) {
        if (subQuery != null) {
            subQuery.setParent(this);
        }
        this.subQuery = subQuery;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.subQuery != null) {
                this.subQuery.accept(visitor);
            }
        }

        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(subQuery);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subQuery == null) ? 0 : subQuery.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLQueryExpr other = (SQLQueryExpr) obj;
        if (subQuery == null) {
            if (other.subQuery != null) {
                return false;
            }
        } else if (!subQuery.equals(other.subQuery)) {
            return false;
        }
        return true;
    }

    public SQLQueryExpr clone() {
        SQLQueryExpr x = new SQLQueryExpr();

        if (subQuery != null) {
            x.setSubQuery(subQuery.clone());
        }
        x.parenthesized = this.parenthesized;

        return x;
    }

    public SQLDataType computeDataType() {
        if (subQuery == null) {
            return null;
        }

        SQLSelectQueryBlock queryBlock = subQuery.getFirstQueryBlock();
        if (queryBlock == null) {
            return null;
        }

        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        if (selectList.size() == 1) {
            return selectList.get(0).computeDataType();
        }

        return null;
    }
}
