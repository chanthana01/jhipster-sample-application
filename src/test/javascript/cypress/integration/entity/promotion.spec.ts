import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Promotion e2e test', () => {
  const promotionPageUrl = '/promotion';
  const promotionPageUrlPattern = new RegExp('/promotion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/promotions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/promotions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/promotions/*').as('deleteEntityRequest');
  });

  it('should load Promotions', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('promotion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Promotion').should('exist');
    cy.url().should('match', promotionPageUrlPattern);
  });

  it('should load details Promotion page', function () {
    cy.visit(promotionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('promotion');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', promotionPageUrlPattern);
  });

  it('should load create Promotion page', () => {
    cy.visit(promotionPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Promotion');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', promotionPageUrlPattern);
  });

  it('should load edit Promotion page', function () {
    cy.visit(promotionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Promotion');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', promotionPageUrlPattern);
  });

  it('should create an instance of Promotion', () => {
    cy.visit(promotionPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Promotion');

    cy.get(`[data-cy="promotionDescription"]`).type('intelligence').should('have.value', 'intelligence');

    cy.get(`[data-cy="promorionFormular"]`).type('Botswana Small Expanded').should('have.value', 'Botswana Small Expanded');

    cy.get(`[data-cy="expireAt"]`).type('2021-08-28T08:19').should('have.value', '2021-08-28T08:19');

    cy.get(`[data-cy="createAt"]`).type('2021-08-27T15:34').should('have.value', '2021-08-27T15:34');

    cy.get(`[data-cy="modifyAt"]`).type('2021-08-27T11:28').should('have.value', '2021-08-27T11:28');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', promotionPageUrlPattern);
  });

  it('should delete last instance of Promotion', function () {
    cy.intercept('GET', '/api/promotions/*').as('dialogDeleteRequest');
    cy.visit(promotionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('promotion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', promotionPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
