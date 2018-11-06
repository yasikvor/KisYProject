package com.kis.loader.dbloader;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.BatchQueryConstants;
import com.kis.constant.GrammarConstants;
import com.kis.constant.LazyQueryConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.annotation.LoaderInfo;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;

import java.sql.SQLException;

@LoaderInfo(type = GrammarConstants.NodeNames.PROCEDURE)
class ProcedureLoader extends BaseLoader {

    @Override
    public TreeNode load() throws SQLException {
        return load(
                LazyQueryConstants.SELECT_PROCEDURES,
                GrammarConstants.CategoryNames.PROCEDURES,
                GrammarConstants.NodeNames.PROCEDURE);
    }

    @Override
    public TreeNode reload(TreeNode procedure) throws ObjectNotFoundException, SQLException {
        return reload(LazyQueryConstants.SELECT_ROUTINES_FOR_REFRESH, procedure);
    }

    @Override
    public void loadDetails(TreeNode procedure) throws SQLException {
        procedure.setChildren(loadDetails(procedure,
                GrammarConstants.NodeNames.PARAMETER,
                LazyQueryConstants.SELECT_PROCEDURE_PARAMETERS));
    }

    @Override
    public TreeNode fullLoad() throws SQLException {
        return fullLoad(
                BatchQueryConstants.SELECT_PROCEDURE_PARAMETERS,
                GrammarConstants.NodeNames.PARAMETER,
                AttributeNameConstants.SPECIFIC_NAME);
    }
}
