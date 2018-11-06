package com.kis.loader.dbloader;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.BatchQueryConstants;
import com.kis.constant.GrammarConstants;
import com.kis.constant.LazyQueryConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.annotation.LoaderInfo;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;

import java.sql.SQLException;

@LoaderInfo(type = GrammarConstants.NodeNames.FUNCTION)
class FunctionLoader extends BaseLoader {

    @Override
    public TreeNode load() throws SQLException {
        return load(
                LazyQueryConstants.SELECT_FUNCTIONS,
                GrammarConstants.CategoryNames.FUNCTIONS,
                GrammarConstants.NodeNames.FUNCTION);
    }

    @Override
    public TreeNode reload(TreeNode function) throws ObjectNotFoundException, SQLException {
        return reload(LazyQueryConstants.SELECT_ROUTINES_FOR_REFRESH, function);
    }

    @Override
    public void loadDetails(TreeNode function) throws SQLException {
        function.setChildren(loadDetails(function,
                GrammarConstants.NodeNames.PARAMETER,
                LazyQueryConstants.SELECT_FUNCTION_PARAMETERS));
    }

    @Override
    public TreeNode fullLoad() throws SQLException {
        return fullLoad(
                BatchQueryConstants.SELECT_FUNCTION_PARAMETERS,
                GrammarConstants.NodeNames.PARAMETER,
                AttributeNameConstants.SPECIFIC_NAME);
    }
}
