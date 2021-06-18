package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.parser.imlang.IMLangBaseVisitor;
import org.endeavourhealth.imapi.parser.imlang.IMLangParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.common.dal.IMLValidatorDAL;
import org.endeavourhealth.informationmanager.common.dal.IMLValidatorJDBC;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IMLSemanticCheck extends IMLangBaseVisitor<TTIriRef> {
	private IMLValidator validator;
	private List<TTIriRef> entities =new ArrayList<>();
	private TTManager manager= new TTManager();
	private TTContext prefixes;
	private IMLValidatorDAL dal;

	public IMLSemanticCheck(IMLValidator validator) throws SQLException, ClassNotFoundException {
		this.validator= validator;
		dal= new IMLValidatorJDBC();
	}

	@Override public TTIriRef visitEntity(IMLangParser.EntityContext ctx)  {
		manager.createDefaultContext();
		if (ctx.iriLabel()==null) {
			validator.addSemanticError("Entity needs iri");
			return null;
		} else if (ctx.iriLabel().iri()==null) {
			validator.addSemanticError("Entity needs an iri identifier");
			return null;
		} else if (!validateNewIri(ctx.iriLabel().iri().getText())) {
			validator.addSemanticError("Entity needs a valid iri");
			return null;
		}	else if (ctx.types()!=null) {
			return visitTypes(ctx.types());
		}
		else
			return TTIriRef.iri(ctx.iriLabel().iri().getText());
	}

	@Override
	public TTIriRef visitTypes(IMLangParser.TypesContext ctx){
		if (ctx.iri()!=null) {
			for (IMLangParser.IriContext iriRule : ctx.iri()) {
				if (!validateTypeIri(iriRule.getText())) {
					validator.addSemanticError("Entity needs valid type(s)");
					return null;
				}
			}
			return null;
		} else return null;

	}

	private boolean validateTypeIri(String testIri) {
		if (!manager.isValidIri(testIri)){
			validator.addSemanticError("invalid type iri");
			return false;
		} else if (!manager.isA(TTIriRef.iri(manager.getContext().expand(testIri)),
			           TTIriRef.iri(IM.NAMESPACE+"Entity"))){
			validator.addSemanticError("type is not a entity type");
			return false;
		}
		return true;
	}


	private boolean validateNewIri(String testIri) {
		if (!manager.isValidIri(testIri)) {
			validator.addSemanticError("invalid iri");
			return false;
		}
		return true;
	}
}




