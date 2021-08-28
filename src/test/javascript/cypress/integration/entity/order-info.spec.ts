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

describe('OrderInfo e2e test', () => {
  const orderInfoPageUrl = '/order-info';
  const orderInfoPageUrlPattern = new RegExp('/order-info(\\?.*)?$');
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
    cy.intercept('GET', '/api/order-infos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-infos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-infos/*').as('deleteEntityRequest');
  });

  it('should load OrderInfos', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-info');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderInfo').should('exist');
    cy.url().should('match', orderInfoPageUrlPattern);
  });

  it('should load details OrderInfo page', function () {
    cy.visit(orderInfoPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('orderInfo');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderInfoPageUrlPattern);
  });

  it('should load create OrderInfo page', () => {
    cy.visit(orderInfoPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrderInfo');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderInfoPageUrlPattern);
  });

  it('should load edit OrderInfo page', function () {
    cy.visit(orderInfoPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('OrderInfo');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderInfoPageUrlPattern);
  });

  it('should create an instance of OrderInfo', () => {
    cy.visit(orderInfoPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrderInfo');

    cy.get(`[data-cy="quantity"]`).type('12946').should('have.value', '12946');

    cy.get(`[data-cy="pricePerUnit"]`).type('12009').should('have.value', '12009');

    cy.get(`[data-cy="createAt"]`).type('2021-08-28T02:41').should('have.value', '2021-08-28T02:41');

    cy.get(`[data-cy="modifyAt"]`).type('2021-08-27T23:59').should('have.value', '2021-08-27T23:59');

    cy.setFieldSelectToLastOfEntity('promotionInfo');

    cy.setFieldSelectToLastOfEntity('product');

    cy.setFieldSelectToLastOfEntity('order');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderInfoPageUrlPattern);
  });

  it('should delete last instance of OrderInfo', function () {
    cy.intercept('GET', '/api/order-infos/*').as('dialogDeleteRequest');
    cy.visit(orderInfoPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('orderInfo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderInfoPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
