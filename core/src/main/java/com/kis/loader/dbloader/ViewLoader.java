package com.kis.loader.dbloader;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.BatchQueryConstants;
import com.kis.constant.GrammarConstants;
import com.kis.constant.LazyQueryConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.annotation.LoaderInfo;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;

import java.sql.SQLException;

@LoaderInfo(type = GrammarConstants.NodeNames.VIEW)
class ViewLoader extends BaseLoader {

    @Override
    public TreeNode load() throws SQLException {
        return load(
                LazyQueryConstants.SELECT_VIEWS,
                GrammarConstants.CategoryNames.VIEWS,
                GrammarConstants.NodeNames.VIEW);
    }

    @Override
    public TreeNode reload(TreeNode view) throws ObjectNotFoundException, SQLException {
        return reload(LazyQueryConstants.SELECT_TABLES_FOR_REFRESH, view);
    }

    @Override
    public void loadDetails(TreeNode view) throws SQLException {
        view.setChildren(loadDetails(view,
                GrammarConstants.NodeNames.COLUMN,
                LazyQueryConstants.SELECT_VIEW_COLUMNS));
    }

    @Override
    public TreeNode fullLoad() throws SQLException {
        return fullLoad(
                BatchQueryConstants.SELECT_VIEW_COLUMNS,
                GrammarConstants.NodeNames.COLUMN,
                AttributeNameConstants.TABLE_NAME);
    }
}
